package com.fds.userservice.controller.user;

import com.fds.userservice.dto.user.*;
import com.fds.userservice.servise.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
        Page<UserResponse> allUsers = userService.findAllUsers(pageable);
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping
    public ResponseEntity<UserResponseWithPassword> registerUser(
            @RequestBody @Valid UserRegistrationRequest customerDto
    ) {
        UserResponseWithPassword createdCustomer = userService.registerUser(customerDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        UserResponse userById = userService.findUserById(id);

        return ResponseEntity.ok(userById);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateCustomer(
            @PathVariable UUID id,
            @RequestBody UserAdminUpdateRequest request
    ) {
        UserResponse updatedCustomer = userService.updateUser(id, request);

        return ResponseEntity.ok(updatedCustomer);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateCustomerRole(@PathVariable UUID id, @RequestParam String role) {
        UserResponse updated = userService.updateRole(id, role);

        return ResponseEntity.ok(updated);
    }
}
