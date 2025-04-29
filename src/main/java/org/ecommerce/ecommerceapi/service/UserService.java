package org.ecommerce.ecommerceapi.service;

import org.ecommerce.ecommerceapi.model.user.Role;
import org.ecommerce.ecommerceapi.model.user.UserDTO;
import org.ecommerce.ecommerceapi.model.user.UserEntity;
import org.ecommerce.ecommerceapi.model.user.UserUpdateDTO;
import org.ecommerce.ecommerceapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Boolean doesUserExist(Integer userId) {
        return userRepository.existsById(userId);
    }

    public Optional<UserEntity> getUserInformation(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity updateUser(UserEntity user, UserUpdateDTO request) {
        if (request.getName() != null) user.setName(request.getName());
        if (request.getUsername() != null) user.setUsername(request.getUsername());

        return userRepository.save(user);
    }

    public UserEntity promoteToAdmin(UserEntity user) {
        user.setRole(Role.ADMIN);
        return userRepository.save(user);
    }
}
