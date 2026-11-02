package com.fds.userservice.dto.address;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DeliveryAddressRequest(
        @NotBlank
        String city,

        @NotBlank
        String street,

        @NotBlank
        String building,

        @NotBlank
        String apartment,

        String entrance,

        String floor,

        @Min(-90)
        @Max(90)
        @NotNull
        BigDecimal latitude,

        @Min(-180)
        @Max(180)
        @NotNull
        BigDecimal longitude
) {
}
