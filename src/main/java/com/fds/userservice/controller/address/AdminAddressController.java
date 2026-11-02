package com.fds.userservice.controller.address;

import com.fds.userservice.dto.address.DeliveryAddressRequest;
import com.fds.userservice.dto.address.DeliveryAddressResponse;
import com.fds.userservice.servise.address.DeliveryAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminAddressController {


    private final DeliveryAddressService addressService;

    @GetMapping("/addresses")
    public ResponseEntity<Page<DeliveryAddressResponse>> getAddresses(Pageable pageable) {
        Page<DeliveryAddressResponse> pages = addressService.getDeliveryAddresses(pageable);

        return ResponseEntity.ok(pages);
    }

    @PostMapping("/addresses")
    public ResponseEntity<DeliveryAddressResponse> createAddress(
            @RequestParam UUID userId,
            @RequestBody @Valid DeliveryAddressRequest request
    ) {
        DeliveryAddressResponse created = addressService.createDeliveryAddress(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/users/{id}/addresses")
    public ResponseEntity<List<DeliveryAddressResponse>> getAddresses(@PathVariable UUID id) {
        List<DeliveryAddressResponse> userAddresses = addressService.getDeliveryAddresses(id);

        return ResponseEntity.ok(userAddresses);
    }
}
