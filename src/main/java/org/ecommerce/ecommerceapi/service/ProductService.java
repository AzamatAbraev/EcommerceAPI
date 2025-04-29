package org.ecommerce.ecommerceapi.service;

import org.ecommerce.ecommerceapi.model.product.Product;
import org.ecommerce.ecommerceapi.model.product.ProductRequestDTO;
import org.ecommerce.ecommerceapi.repository.ProductRepository;
import org.ecommerce.ecommerceapi.utils.ProductSpecifications;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getAllProducts(String name, String description, Pageable pageable) {
        var spec = Specification.where(ProductSpecifications.nameContains(name)).or(ProductSpecifications.descriptionContains(description));
        return productRepository.findAll(spec, pageable);
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }
}
