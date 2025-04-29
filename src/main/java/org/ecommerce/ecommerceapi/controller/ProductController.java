package org.ecommerce.ecommerceapi.controller;

import jakarta.validation.Valid;
import org.ecommerce.ecommerceapi.exception.CustomNotFoundException;
import org.ecommerce.ecommerceapi.model.api.ApiResponse;
import org.ecommerce.ecommerceapi.model.product.ProductRequestDTO;
import org.ecommerce.ecommerceapi.service.ProductService;
import org.ecommerce.ecommerceapi.utils.Mapper;
import org.ecommerce.ecommerceapi.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final Mapper mapper;

    @Autowired
    public ProductController(ProductService productService, Mapper mapper) {
        this.productService = productService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getProducts(
            @RequestParam String name,
            @RequestParam String description,
            Pageable pageable
    ) {
        var productEntities = productService.getAllProducts(name, description, pageable);
        var products = productEntities.stream().map(mapper::toDTO).toList();
        return ResponseBuilder.build(HttpStatus.OK, "Success", products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Integer id) {
        var product = productService.getProductById(id).orElseThrow(() -> new CustomNotFoundException("Product with id " + id + " is not found"));
        return ResponseBuilder.build(HttpStatus.OK, "Success", mapper.toDTO(product));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        var product = mapper.toEntity(request);
        var savedProduct = productService.saveProduct(product);
        return ResponseBuilder.build(HttpStatus.CREATED, "Product created!", mapper.toDTO(savedProduct));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Integer id, @Valid @RequestBody ProductRequestDTO request) {
        var existingProduct = productService.getProductById(id).orElseThrow(() -> new CustomNotFoundException("Product with id " + id + " is not found"));
        var updatedEntity = mapper.toEntity(request);
        updatedEntity.setId(id);
        var updatedProduct = productService.saveProduct(updatedEntity);
        return ResponseBuilder.build(HttpStatus.OK, "Product updated successfully!", mapper.toDTO(updatedProduct));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Integer id) {
        var product = productService.getProductById(id).orElseThrow(() -> new CustomNotFoundException("Product with id " + id + " is not found"));
        productService.deleteProduct(product);
        return ResponseBuilder.build(HttpStatus.OK, "Product with id " + id + " deleted successfully", null);
    }
}
