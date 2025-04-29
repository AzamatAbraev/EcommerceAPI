package org.ecommerce.ecommerceapi.controller;

import org.ecommerce.ecommerceapi.exception.BadRequestException;
import org.ecommerce.ecommerceapi.exception.CustomNotFoundException;
import org.ecommerce.ecommerceapi.model.api.ApiResponse;
import org.ecommerce.ecommerceapi.model.order.OrderStatus;
import org.ecommerce.ecommerceapi.model.order.OrderStatusChangeRequest;
import org.ecommerce.ecommerceapi.model.user.UserIDRequest;
import org.ecommerce.ecommerceapi.service.OrderService;
import org.ecommerce.ecommerceapi.service.ShoppingCartService;
import org.ecommerce.ecommerceapi.service.UserService;
import org.ecommerce.ecommerceapi.utils.Mapper;
import org.ecommerce.ecommerceapi.utils.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final ShoppingCartService cartService;
    private final Mapper mapper;

    public OrderController(OrderService orderService, UserService userService, ShoppingCartService cartService, Mapper mapper) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllOrdersForUser(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.getUserInformation(userDetails.getUsername()).orElseThrow(() -> new CustomNotFoundException("User is not found"));
        var orders = orderService.getAllOrdersForUser(user.getId());

        return ResponseBuilder.build(HttpStatus.OK, "Success!", orders.stream().map(mapper::toDTO).toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllOrders() {
        var orders = orderService.getAllOrders();
        return ResponseBuilder.build(HttpStatus.OK, "Success!", orders.stream().map(mapper::toDTO).toList());
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Integer orderId, @RequestParam Integer userId) {
        if (!userService.doesUserExist(userId)) {
            throw new CustomNotFoundException("User with id " + userId + " is not found");
        }

        var order = orderService.getOrderById(orderId, userId).orElseThrow(() -> new CustomNotFoundException("Order with id " + orderId + " is not found"));

        var response = new ApiResponse();
        response.setMessage("Success");
        response.setStatus(HttpStatus.OK);
        response.setCode(HttpStatus.OK.value());
        response.setData(mapper.toDTO(order));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createOrderFromCart(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.getUserInformation(userDetails.getUsername()).orElseThrow(() -> new CustomNotFoundException("User is not found"));
        var cart = cartService.getUserCart(user);

        var order = orderService.createOrder(user, cart);

        return ResponseBuilder.build(HttpStatus.CREATED, "Order created!", mapper.toDTO(order));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/shipped")
    public ResponseEntity<ApiResponse> updateOrderStatusToShipped(@PathVariable Integer orderId, @RequestParam Integer userId) {
        if (!userService.doesUserExist(userId)) {
            throw new CustomNotFoundException("User with id " + userId + " is not found");
        }

        var order = orderService.getOrderById(orderId, userId).orElseThrow(() -> new CustomNotFoundException("Order is not found"));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("You cannot change order status after it is delivered");
        }

        if (order.getStatus() == OrderStatus.SHIPPED) {
            throw new BadRequestException("The order has already been shipped");
        }

        var updatedOrder = orderService.updateOrderStatusToShipped(order);
        return ResponseBuilder.build(HttpStatus.OK, "Order has been shipped!", mapper.toDTO(updatedOrder));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/delivered")
    public ResponseEntity<ApiResponse> updateOrderStatusToDelivered(@PathVariable Integer orderId, @RequestParam Integer userId) {
        if (!userService.doesUserExist(userId)) {
            throw new CustomNotFoundException("User with id " + userId + " is not found");
        }

        var order = orderService.getOrderById(orderId, userId).orElseThrow(() -> new CustomNotFoundException("Order is not found"));

        if (order.getStatus() == OrderStatus.PENDING) {
            throw new BadRequestException("Order status cannot be changed to delivered before it is not even shipped");
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("The order has already been delivered");
        }

        var updatedOrder = orderService.updateOrderStatusToDelivered(order);
        return ResponseBuilder.build(HttpStatus.OK, "Order has been delivered!", mapper.toDTO(updatedOrder));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Integer orderId, @RequestParam Integer userId) {
        if (!userService.doesUserExist(userId)) {
            throw new CustomNotFoundException("User with id " + userId + " is not found");
        }

        var order = orderService.getOrderById(orderId, userId).orElseThrow(() -> new CustomNotFoundException("Order is not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Order cannot be cancelled after it is shipped or delivered.");
        }

        var updatedOrder = orderService.cancelOrder(order);
        return ResponseBuilder.build(HttpStatus.OK, "Order has been cancelled!", mapper.toDTO(updatedOrder));
    }
}
