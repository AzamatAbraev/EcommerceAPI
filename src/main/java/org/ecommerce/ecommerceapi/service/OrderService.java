package org.ecommerce.ecommerceapi.service;

import org.aspectj.weaver.ast.Or;
import org.ecommerce.ecommerceapi.model.cart.CartItem;
import org.ecommerce.ecommerceapi.model.cart.ShoppingCart;
import org.ecommerce.ecommerceapi.model.order.Order;
import org.ecommerce.ecommerceapi.model.order.OrderItem;
import org.ecommerce.ecommerceapi.model.order.OrderStatus;
import org.ecommerce.ecommerceapi.model.user.UserEntity;
import org.ecommerce.ecommerceapi.repository.OrderRepository;
import org.ecommerce.ecommerceapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrdersForUser(Integer userId) {
        return orderRepository.findAllByUser_Id(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Integer orderId, Integer userId) {
        return orderRepository.findByIdAndUser_Id(orderId, userId);
    }

    public Order createOrder(UserEntity user, ShoppingCart cart) {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Shopping cart is empty or null.");
        }

        Order order = new Order();
        order.setUser(user);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            var product = cartItem.getProduct();
            BigDecimal itemPrice = product.getPrice();
            Integer quantity = cartItem.getQuantity();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPriceAtPurchase(itemPrice);

            orderItems.add(orderItem);
            totalPrice = totalPrice.add(itemPrice.multiply(BigDecimal.valueOf(quantity)));
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalPrice);

        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Order order, OrderStatus status) {
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order updateOrderStatusToShipped(Order order) {
        order.setStatus(OrderStatus.SHIPPED);
        return orderRepository.save(order);
    }

    public Order updateOrderStatusToDelivered(Order order) {
        order.setStatus(OrderStatus.DELIVERED);
        return orderRepository.save(order);
    }

    public Order cancelOrder(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

}
