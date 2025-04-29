package org.ecommerce.ecommerceapi.controller;

import jakarta.validation.Valid;
import org.ecommerce.ecommerceapi.exception.CustomNotFoundException;
import org.ecommerce.ecommerceapi.model.api.ApiResponse;
import org.ecommerce.ecommerceapi.model.user.UserDTO;
import org.ecommerce.ecommerceapi.model.user.UserEntity;
import org.ecommerce.ecommerceapi.model.user.UserUpdateDTO;
import org.ecommerce.ecommerceapi.service.UserService;
import org.ecommerce.ecommerceapi.utils.Mapper;
import org.ecommerce.ecommerceapi.utils.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@EnableMethodSecurity
public class UserController {
    private final UserService userService;
    private final Mapper mapper;

    public UserController(UserService userService, Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() {
        var usersList = userService.getAllUsers();
        return ResponseBuilder.build(HttpStatus.OK, "Success!", usersList.stream().map(mapper::toDTO).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("id") Integer id) {
        var user = userService.getUserById(id).orElseThrow(() -> new CustomNotFoundException("User with id " + id + " is not found"));

        return ResponseBuilder.build(HttpStatus.OK, "Success!", mapper.toDTO(user));
    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponse> getUserInformation(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.getUserInformation(userDetails.getUsername()).orElseThrow(() -> new CustomNotFoundException("User with username " + userDetails.getUsername() + " is not found"));

        return ResponseBuilder.build(HttpStatus.OK, "Success!", mapper.toDTO(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<ApiResponse> updateUser(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UserUpdateDTO request) {

        var user = userService.getUserById(id).orElseThrow(() -> new CustomNotFoundException("User with id " + id + " is not found"));
        var updatedUser = userService.updateUser(user, request);
        return ResponseBuilder.build(HttpStatus.OK, "User updated successfully!", mapper.toDTO(updatedUser));
    }

    @PutMapping("/{id}/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> promoteToAdmin(@PathVariable Integer id) {
        var user = userService.getUserById(id).orElseThrow(() -> new CustomNotFoundException("User with id " + id + " is not found"));
        var adminUser = userService.promoteToAdmin(user);
        return ResponseBuilder.build(HttpStatus.OK, user.getUsername() + " is promoted to admin", mapper.toDTO(adminUser));
    }
}
