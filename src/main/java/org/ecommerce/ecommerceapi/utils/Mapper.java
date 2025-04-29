package org.ecommerce.ecommerceapi.utils;

import org.ecommerce.ecommerceapi.model.cart.CartItem;
import org.ecommerce.ecommerceapi.model.cart.CartItemResponseDTO;
import org.ecommerce.ecommerceapi.model.cart.ShoppingCart;
import org.ecommerce.ecommerceapi.model.cart.ShoppingCartResponseDTO;
import org.ecommerce.ecommerceapi.model.order.Order;
import org.ecommerce.ecommerceapi.model.order.OrderItem;
import org.ecommerce.ecommerceapi.model.order.OrderItemResponseDTO;
import org.ecommerce.ecommerceapi.model.order.OrderResponseDTO;
import org.ecommerce.ecommerceapi.model.product.Product;
import org.ecommerce.ecommerceapi.model.product.ProductRequestDTO;
import org.ecommerce.ecommerceapi.model.product.ProductResponseDTO;
import org.ecommerce.ecommerceapi.model.user.UserDTO;
import org.ecommerce.ecommerceapi.model.user.UserEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {
    public UserDTO toDTO(UserEntity userEntity) {
        var user = new UserDTO();

        user.setId(userEntity.getId());
        user.setActive(userEntity.isActive());
        user.setName(userEntity.getName());
        user.setRole(userEntity.getRole());
        user.setUsername(userEntity.getUsername());
        user.setCreatedAt(userEntity.getCreatedAt());
        user.setUpdatedAt(userEntity.getUpdatedAt());

        return user;
    }

    public ProductResponseDTO toDTO(Product productEntity) {
        var product = new ProductResponseDTO();
        product.setId(productEntity.getId());
        product.setDescription(productEntity.getDescription());
        product.setName(productEntity.getName());
        product.setPrice(productEntity.getPrice());
        product.setStock(productEntity.getStock());
        product.setImageUrl(productEntity.getImageUrl());
        product.setCreatedAt(productEntity.getCreatedAt());
        product.setUpdatedAt(productEntity.getUpdatedAt());

        return product;
    }

    public Product toEntity(ProductRequestDTO request) {
        var product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setStock(request.getStock());

        return product;
    }

    public ShoppingCartResponseDTO toDTO(ShoppingCart cartEntity) {
        List<CartItemResponseDTO> cartItems = cartEntity.getItems().stream().map(this::toDTO).toList();
        BigDecimal totalPrice = cartItems.stream().map(CartItemResponseDTO::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);


        var cart = new ShoppingCartResponseDTO();
        cart.setId(cartEntity.getId());
        cart.setUserId(cartEntity.getUser().getId());
        cart.setItems(cartItems);
        cart.setTotalPrice(totalPrice);
        cart.setCreatedAt(cartEntity.getCreatedAt());
        cart.setUpdatedAt(cartEntity.getUpdatedAt());

        return cart;
    }

    public CartItemResponseDTO toDTO(CartItem item) {
        var price = item.getProduct().getPrice();
        var quantity = item.getQuantity();

        var cartItem = new CartItemResponseDTO();
        cartItem.setId(item.getId());
        cartItem.setProductName(item.getProduct().getName());
        cartItem.setProductPrice(item.getProduct().getPrice());
        cartItem.setQuantity(item.getQuantity());
        cartItem.setTotal(price.multiply(BigDecimal.valueOf(quantity)));

        return cartItem;
    }

    public OrderItemResponseDTO toDTO(OrderItem item) {
        var orderItem = new OrderItemResponseDTO();
        orderItem.setProductName(item.getProduct().getName());
        orderItem.setProductId(item.getProduct().getId());
        orderItem.setQuantity(item.getQuantity());
        orderItem.setPriceAtPurchase(item.getPriceAtPurchase());
        orderItem.setTotal(item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())));

        return orderItem;
    }

    public OrderResponseDTO toDTO(Order orderEntity) {
        var items = orderEntity.getItems().stream().map(this::toDTO).toList();
        var totalPrice = items.stream().map(OrderItemResponseDTO::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        var order = new OrderResponseDTO();
        order.setId(orderEntity.getId());
        order.setCreatedAt(orderEntity.getCreatedAt());
        order.setUserId(orderEntity.getUser().getId());
        order.setStatus(orderEntity.getStatus());
        order.setItems(items);
        order.setTotalAmount(totalPrice);
        order.setCreatedAt(orderEntity.getCreatedAt());

        return order;
    }



}
