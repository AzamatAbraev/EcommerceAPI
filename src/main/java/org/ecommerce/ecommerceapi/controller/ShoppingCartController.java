package org.ecommerce.ecommerceapi.controller;

import jakarta.validation.Valid;
import org.ecommerce.ecommerceapi.exception.CustomNotFoundException;
import org.ecommerce.ecommerceapi.model.api.ApiResponse;
import org.ecommerce.ecommerceapi.model.cart.CartItem;
import org.ecommerce.ecommerceapi.model.cart.CartItemRequestDTO;
import org.ecommerce.ecommerceapi.model.user.UserEntity;
import org.ecommerce.ecommerceapi.service.ProductService;
import org.ecommerce.ecommerceapi.service.ShoppingCartService;
import org.ecommerce.ecommerceapi.service.UserService;
import org.ecommerce.ecommerceapi.utils.Mapper;
import org.ecommerce.ecommerceapi.utils.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final ProductService productService;
    private final Mapper mapper;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService, ProductService productService, Mapper mapper) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.productService = productService;
        this.mapper = mapper;
    }

    @RequestMapping
    public ResponseEntity<ApiResponse> getShoppingCart(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.getUserInformation(userDetails.getUsername()).orElseThrow(() -> new CustomNotFoundException("User with username ( " + userDetails.getUsername() +  " ) is not found"));

        var cart = shoppingCartService.getUserCart(user);

        var apiResponse = new ApiResponse();
        apiResponse.setMessage("Success");
        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setData(mapper.toDTO(cart));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@Valid @RequestBody CartItemRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.getUserInformation(userDetails.getUsername()).orElseThrow(() -> new CustomNotFoundException("User is not found"));
        var product = productService.getProductById(request.getProductId()).orElseThrow(() -> new CustomNotFoundException("Product is not found"));

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(request.getQuantity());

        shoppingCartService.addToCart(user, item);
        return ResponseBuilder.build(HttpStatus.ACCEPTED, "Item added to the cart successfully", null);
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<ApiResponse> removeFromCart(@PathVariable("itemId") Integer itemId, @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.getUserInformation(userDetails.getUsername()).orElseThrow(() -> new CustomNotFoundException("User is not found"));
        shoppingCartService.removeFromCart(user, itemId);
        return ResponseBuilder.build(HttpStatus.ACCEPTED, "Item removed from cart", null);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getUserInformation(userDetails.getUsername()).orElseThrow(() -> new CustomNotFoundException("User is not found"));
        shoppingCartService.clearCart(user);
        return ResponseBuilder.build(HttpStatus.ACCEPTED, "Cart cleared", null);
    }

}
