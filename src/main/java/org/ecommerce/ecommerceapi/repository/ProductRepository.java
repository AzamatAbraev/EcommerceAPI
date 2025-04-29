package org.ecommerce.ecommerceapi.repository;

import org.ecommerce.ecommerceapi.model.product.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    @NotNull List<Product> findAll(Specification<Product> specification);
}
