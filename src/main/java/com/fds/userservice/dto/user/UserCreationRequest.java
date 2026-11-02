package com.fds.userservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserCreationRequest(
        @NotNull
        UUID id,
        @Email
        @NotNull
        String email
) {
}
