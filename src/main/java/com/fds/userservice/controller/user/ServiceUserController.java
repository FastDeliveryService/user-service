package com.fds.userservice.controller.user;

import com.fds.userservice.dto.user.UserCreationRequest;
import com.fds.userservice.dto.user.UserResponse;
import com.fds.userservice.servise.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/service/customers")
@RequiredArgsConstructor
public class ServiceUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createCustomer(
            @RequestBody @Valid UserCreationRequest customerDto
    ) {
        UserResponse createdCustomer = userService.saveToDatabase(customerDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }
}
