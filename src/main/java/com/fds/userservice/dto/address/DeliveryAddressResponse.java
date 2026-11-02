package com.fds.userservice.dto.address;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record DeliveryAddressResponse(
        Long id,
        UUID userId,
        String city,
        String street,
        String building,
        String apartment,
        String entrance,
        String floor,
        BigDecimal latitude,
        BigDecimal longitude,
        Boolean defaultAddress,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
