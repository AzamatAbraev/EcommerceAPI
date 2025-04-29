package org.ecommerce.ecommerceapi.controller;

import jakarta.validation.Valid;
import org.ecommerce.ecommerceapi.model.api.ApiResponse;
import org.ecommerce.ecommerceapi.model.auth.LoginRequest;
import org.ecommerce.ecommerceapi.model.auth.RegisterRequest;
import org.ecommerce.ecommerceapi.service.AuthService;
import org.ecommerce.ecommerceapi.utils.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        var response = authService.register(request);
        return ResponseBuilder.build(HttpStatus.CREATED, "User registered successfully", response);
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        var response = authService.login(request);
        return ResponseBuilder.build(HttpStatus.OK, "User signed in successfully", response);
    }

}
