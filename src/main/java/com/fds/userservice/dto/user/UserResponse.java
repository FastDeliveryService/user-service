package com.fds.userservice.dto.user;

import com.fds.userservice.dto.address.DeliveryAddressResponse;
import com.fds.userservice.enums.Role;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        Boolean active,
        Role role,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<DeliveryAddressResponse> addresses
) {
}
