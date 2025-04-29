package org.ecommerce.ecommerceapi.model.payment;

import java.time.LocalDateTime;

public class PaymentDTO {
    private Integer id;
    private Integer orderId;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paidAt;
}
