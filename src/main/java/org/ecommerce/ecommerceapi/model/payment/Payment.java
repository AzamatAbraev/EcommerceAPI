package org.ecommerce.ecommerceapi.model.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ecommerce.ecommerceapi.model.order.Order;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private String paymentMethod;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;
    private LocalDateTime paidAt;

    @PrePersist
    public void onCreate() {
        this.paymentStatus = PaymentStatus.PENDING;
    }
}
