package org.ecommerce.ecommerceapi.utils;

import org.ecommerce.ecommerceapi.model.product.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> descriptionContains(String description) {
        return (root, query, cb) -> {
            if (description == null || description.isBlank()) return null;
            return cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
        };
    }
}
