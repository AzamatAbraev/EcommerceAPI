package org.ecommerce.ecommerceapi.repository;

import org.ecommerce.ecommerceapi.model.cart.ShoppingCart;
import org.ecommerce.ecommerceapi.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    Optional<ShoppingCart> findByUserId(Integer userId);
}
