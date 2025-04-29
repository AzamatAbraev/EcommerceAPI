package org.ecommerce.ecommerceapi.model.order;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDTO {
    private Integer productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}

