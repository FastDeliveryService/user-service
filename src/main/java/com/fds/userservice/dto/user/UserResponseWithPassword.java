package com.fds.userservice.dto.user;

import com.fds.userservice.enums.Role;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponseWithPassword(
        UUID id,
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        Boolean active,
        Role role,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
