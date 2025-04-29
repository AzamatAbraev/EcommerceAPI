package org.ecommerce.ecommerceapi.model.order;

import lombok.Getter;

@Getter
public class OrderStatusChangeRequest {
    private OrderStatus status;
}
