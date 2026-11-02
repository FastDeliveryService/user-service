package com.fds.userservice.repository;

import com.fds.userservice.dto.address.DeliveryAddressRequest;
import com.fds.userservice.entity.DeliveryAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddressEntity, Long> {

    List<DeliveryAddressEntity> readAllByUserId(UUID customerId);
    boolean existsByIdAndUserId(Long id, UUID customerId);
    Optional<DeliveryAddressEntity> readByUserIdAndDefaultAddressTrue(UUID customerId);
    boolean existsByUserId(UUID customerId);
}
