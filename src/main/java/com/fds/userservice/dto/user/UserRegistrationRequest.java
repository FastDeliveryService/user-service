package com.fds.userservice.dto.user;

import jakarta.validation.constraints.*;

public record UserRegistrationRequest(

        @Email
        @NotNull
        String email,

        @NotBlank
        String role,

        @NotBlank
        @Size(min = 2, max = 100)
        String firstName,

        @NotBlank
        @Size(min = 2, max = 100)
        String lastName,

        @NotBlank
        String phoneNumber
) {
}
