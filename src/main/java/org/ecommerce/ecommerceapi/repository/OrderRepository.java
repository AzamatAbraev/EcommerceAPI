package org.ecommerce.ecommerceapi.repository;

import org.ecommerce.ecommerceapi.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUser_Id(Integer userId);
    Optional<Order> findByIdAndUser_Id(Integer orderId, Integer userId);
}
