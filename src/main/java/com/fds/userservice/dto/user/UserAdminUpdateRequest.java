package com.fds.userservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserAdminUpdateRequest(
        @Email
        @NotNull
        String email,

        @NotBlank
        @Size(min = 2, max = 100)
        String firstName,

        @NotBlank
        @Size(min = 2, max = 100)
        String lastName,

        @NotNull
        String phoneNumber
) {
}
