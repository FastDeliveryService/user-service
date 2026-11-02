package com.fds.userservice.controller.user;

import com.fds.security.utils.SecurityUtils;
import com.fds.userservice.dto.user.UserPublicUpdateRequest;
import com.fds.userservice.dto.user.UserResponse;
import com.fds.userservice.servise.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("api/v1/profile")
@RequiredArgsConstructor
public class PublicUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> showProfile() {
        UUID id = SecurityUtils.getUserId();

        UserResponse customer = userService.findUserById(id);
        return ResponseEntity.ok(customer);
    }


    @PutMapping
    public ResponseEntity<UserResponse> updateProfile(
            @RequestBody @Valid UserPublicUpdateRequest request
    ) {
        UUID id = SecurityUtils.getUserId();
        UserResponse userResponse = userService.updateUser(id, request);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProfile() {
        userService.deleteUser(SecurityUtils.getUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
