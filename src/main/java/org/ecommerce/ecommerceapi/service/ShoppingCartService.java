package org.ecommerce.ecommerceapi.service;

import org.ecommerce.ecommerceapi.exception.CustomNotFoundException;
import org.ecommerce.ecommerceapi.model.cart.CartItem;
import org.ecommerce.ecommerceapi.model.cart.ShoppingCart;
import org.ecommerce.ecommerceapi.model.user.UserEntity;
import org.ecommerce.ecommerceapi.repository.ShoppingCartRepository;
import org.ecommerce.ecommerceapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public ShoppingCart getUserCart(UserEntity user) {
        return shoppingCartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart();
                    cart.setUser(user);
                    return shoppingCartRepository.save(cart);
                });
    }

    public void addToCart(UserEntity user, CartItem newItem) {
        ShoppingCart cart = getUserCart(user);

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(newItem.getProduct().getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
        } else {
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        shoppingCartRepository.save(cart);
    }

    public void removeFromCart(UserEntity user, Integer cartItemId) {
        ShoppingCart cart = getUserCart(user);
        Optional<CartItem> cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(cartItemId))
                .findFirst();

        if (cartItem.isPresent()) {
            var item = cartItem.get();
            cart.getItems().remove(item);
        } else {
            throw new CustomNotFoundException("Cart item is not found");
        }

        shoppingCartRepository.save(cart);
    }

    public void clearCart(UserEntity user) {
        ShoppingCart cart = getUserCart(user);
        cart.getItems().clear();

        shoppingCartRepository.save(cart);
    }


}
