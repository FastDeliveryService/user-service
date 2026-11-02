package com.fds.userservice.controller.address;

import com.fds.security.utils.SecurityUtils;
import com.fds.userservice.dto.address.DeliveryAddressRequest;
import com.fds.userservice.dto.address.DeliveryAddressResponse;
import com.fds.userservice.servise.address.DeliveryAddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/profile/addresses")
@RequiredArgsConstructor
public class PublicAddressController {

    private final DeliveryAddressService addressService;

    @GetMapping
    public ResponseEntity<List<DeliveryAddressResponse>> getMyAddresses() {
        List<DeliveryAddressResponse> addresses = addressService.getDeliveryAddresses(SecurityUtils.getUserId());
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@addressSecurity.isOwner(#id) or hasRole('ADMIN')")
    public ResponseEntity<DeliveryAddressResponse> getAddress(@PathVariable Long id) {
        DeliveryAddressResponse address = addressService.getDeliveryAddress(id);
        return ResponseEntity.ok(address);
    }

    @PostMapping
    public ResponseEntity<DeliveryAddressResponse> createAddress(@RequestBody @Valid DeliveryAddressRequest request) {
        DeliveryAddressResponse created = addressService.createDeliveryAddress(SecurityUtils.getUserId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryAddressResponse> updateAddress(
            @PathVariable @Min(1) long id,
            @RequestBody @Valid DeliveryAddressRequest request
    ) {
        DeliveryAddressResponse updated = addressService.updateDeliveryAddress(id, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/default")
    @PreAuthorize("@addressSecurity.isOwner(#id) or hasRole('ADMIN')")
    public ResponseEntity<Void> makeDefault(@PathVariable Long id) {
        addressService.makeDefault(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@addressSecurity.isOwner(#id) or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
