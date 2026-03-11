package com.example.nhom1.service;

import com.example.nhom1.model.User;
import com.example.nhom1.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findByDeletedAtIsNull();
    }

    public User getUserById(UUID id) {
        return userRepository.findByIdAndDeletedAtIsNull(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User userData) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow();

        user.setUsername(userData.getUsername());
        user.setPasswordHash(userData.getPasswordHash());
        user.setFullName(userData.getFullName());
        user.setEmail(userData.getEmail());
        user.setPhone(userData.getPhone());
        user.setIsActive(userData.getIsActive());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        deleteById(id);
    }

    public List<User> searchUsers(String name, String username, String email) {
        List<User> result = userRepository.findByDeletedAtIsNull();

        if (name != null && !name.isBlank()) {
            result = result.stream()
                    .filter(u -> u.getFullName() != null
                            && u.getFullName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (username != null && !username.isBlank()) {
            result = result.stream()
                    .filter(u -> u.getUsername() != null
                            && u.getUsername().toLowerCase().contains(username.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (email != null && !email.isBlank()) {
            result = result.stream()
                    .filter(u -> u.getEmail() != null
                            && u.getEmail().toLowerCase().contains(email.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return result;
    }

    public List<User> getActiveUsers(Boolean isActive) {
        return userRepository.findByIsActiveAndDeletedAtIsNull(isActive);
    }

    public Page<User> getUsersPagination(int page, int size, String sort) {
        String[] sortArr = sort.split(",");
        String sortField = mapSortField(sortArr[0]);

        Sort sortObj = Sort.by(
                Sort.Direction.fromString(sortArr[1]),
                sortField
        );

        Pageable pageable = PageRequest.of(page, size, sortObj);

        return userRepository.findByDeletedAtIsNull(pageable);
    }

    public User lockUser(UUID id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow();
        user.setIsActive(false);
        return userRepository.save(user);
    }

    public User unlockUser(UUID id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow();
        user.setIsActive(true);
        return userRepository.save(user);
    }

    public void deleteById(UUID id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow();
        if (user.getDeletedAt() == null) {
            user.setDeletedAt(LocalDateTime.now());
        }
        userRepository.save(user);
    }

    private String mapSortField(String field) {
        if ("full_name".equalsIgnoreCase(field)) {
            return "fullName";
        }
        if ("is_active".equalsIgnoreCase(field)) {
            return "isActive";
        }
        return field;
    }
}
