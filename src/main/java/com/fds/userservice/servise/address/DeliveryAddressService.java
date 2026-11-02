package com.fds.userservice.servise.address;

import com.fds.userservice.dto.address.DeliveryAddressRequest;
import com.fds.userservice.dto.address.DeliveryAddressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface DeliveryAddressService {
    Page<DeliveryAddressResponse> getDeliveryAddresses(Pageable pageable);
    List<DeliveryAddressResponse> getDeliveryAddresses(UUID customerId);
    DeliveryAddressResponse getDeliveryAddress(Long id);
    DeliveryAddressResponse createDeliveryAddress(UUID customerId, DeliveryAddressRequest request);
    DeliveryAddressResponse updateDeliveryAddress(Long id, DeliveryAddressRequest request);
    void makeDefault(Long id);
    void delete(Long id);
}
