package org.ecommerce.ecommerceapi.service;

import org.ecommerce.ecommerceapi.config.ApplicationConfig;
import org.ecommerce.ecommerceapi.model.auth.AuthResponse;
import org.ecommerce.ecommerceapi.model.auth.LoginRequest;
import org.ecommerce.ecommerceapi.model.auth.RegisterRequest;
import org.ecommerce.ecommerceapi.model.user.UserEntity;
import org.ecommerce.ecommerceapi.model.user.Role;
import org.ecommerce.ecommerceapi.repository.UserRepository;
import org.ecommerce.ecommerceapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final ApplicationConfig applicationConfig;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthService(UserRepository userRepository, ApplicationConfig applicationConfig, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.applicationConfig = applicationConfig;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (request.getUsername() == null) {
            throw new RuntimeException("Username is required");
        }

        if (request.getPassword() == null) {
            throw new RuntimeException("Password is required");
        }

        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        if (request.getUsername() == null) {
            throw new RuntimeException("Username is required");
        }

        if (request.getPassword() == null) {
            throw new RuntimeException("Password is required");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(user);

        return new AuthResponse(token);
    }

}
