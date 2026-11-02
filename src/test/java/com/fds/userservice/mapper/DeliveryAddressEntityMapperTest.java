package com.fds.userservice.mapper;

import com.fds.userservice.dto.address.DeliveryAddressRequest;
import com.fds.userservice.dto.address.DeliveryAddressResponse;
import com.fds.userservice.entity.UserEntity;
import com.fds.userservice.entity.DeliveryAddressEntity;
import com.fds.userservice.mappers.DeliveryAddressMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryAddressEntityMapperTest {

    private static final DeliveryAddressMapper mapper = Mappers.getMapper(DeliveryAddressMapper.class);

    @Test
    @DisplayName("DeliveryAddress entity should be mapped correctly to DTO")
    void entityToDto_mapsAllFieldsCorrectly() {

        Long id = 1L;

        DeliveryAddressEntity address = new DeliveryAddressEntity(
                id,
                "Gotham",
                "Wayne Street",
                "Wayne Tower",
                "101",
                "B",
                "12",
                new BigDecimal("40.7128"),
                new BigDecimal("-74.0060"),
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                null
        );

        UserEntity customer = new UserEntity();
        customer.setId(UUID.randomUUID());
        customer.getAddresses().add(address);
        address.setUser(customer);

        DeliveryAddressResponse dto = mapper.entityToDto(address);

        assertNotNull(dto);
        assertEquals(address.getId(), dto.id());
        assertEquals(address.getCity(), dto.city());
        assertEquals(address.getBuilding(), dto.building());
        assertEquals(address.getApartment(), dto.apartment());
        assertEquals(address.getEntrance(), dto.entrance());
        assertEquals(address.getFloor(), dto.floor());
        assertEquals(address.getLatitude(), dto.latitude());
        assertEquals(address.getLongitude(), dto.longitude());
        assertEquals(address.getDefaultAddress(), dto.defaultAddress());
        assertEquals(address.getCreatedAt(), dto.createdAt());
        assertEquals(address.getUpdatedAt(), dto.updatedAt());
        assertEquals(address.getUser().getId(), dto.userId());
    }

    @Test
    @DisplayName("List of DeliveryAddress entities should be mapped correctly to list of DTOs")
    void entityListToDto_mapsListCorrectly() {

        DeliveryAddressEntity address1 = new DeliveryAddressEntity(
                1L,
                "Metropolis",
                "Main St",
                "Daily Planet",
                "202",
                "A",
                "5",
                new BigDecimal("38.8977"),
                new BigDecimal("-77.0365"),
                false,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                null
        );

        DeliveryAddressEntity address2 = new DeliveryAddressEntity(
                2L,
                "Star City",
                "Queen St",
                "Oliver's Hideout",
                "303",
                "C",
                "9",
                new BigDecimal("37.7749"),
                new BigDecimal("-122.4194"),
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                null
        );

        List<DeliveryAddressEntity> addresses = List.of(address1, address2);

        List<DeliveryAddressResponse> dtos = mapper.entityToDto(addresses);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        DeliveryAddressResponse dto1 = dtos.get(0);
        assertEquals(address1.getId(), dto1.id());
        assertEquals(address1.getCity(), dto1.city());
        assertEquals(address1.getBuilding(), dto1.building());

        DeliveryAddressResponse dto2 = dtos.get(1);
        assertEquals(address2.getId(), dto2.id());
        assertEquals(address2.getCity(), dto2.city());
        assertEquals(address2.getBuilding(), dto2.building());
    }
    
    
    @Test
    @DisplayName("DeliveryAddress request should be mapped correctly to entity")
    void dtoToEntity_mapsAllFieldsCorrectly() {

        DeliveryAddressRequest request = new DeliveryAddressRequest(
                "Gotham",
                "Wayne Street",
                "Wayne Tower",
                "101",
                "B",
                "12",
                new BigDecimal("40.7128"),
                new BigDecimal("-74.0060")
        );

        DeliveryAddressEntity entity = mapper.dtoToEntity(request);

        assertNotNull(entity);
        assertEquals(request.city(), entity.getCity());
        assertEquals(request.street(), entity.getStreet());
        assertEquals(request.building(), entity.getBuilding());
        assertEquals(request.apartment(), entity.getApartment());
        assertEquals(request.entrance(), entity.getEntrance());
        assertEquals(request.floor(), entity.getFloor());
        assertEquals(request.latitude(), entity.getLatitude());
        assertEquals(request.longitude(), entity.getLongitude());

        // fields not present in the request should stay null / default
        assertNull(entity.getId());
        assertNull(entity.getUser());
    }

    @Test
    @DisplayName("Update entity with request should modify only mapped fields")
    void updateEntity_updatesMappedFieldsOnly() {

        DeliveryAddressEntity existing = new DeliveryAddressEntity(
                5L,
                "OldCity",
                "OldStreet",
                "OldBuilding",
                "OldApartment",
                "OldEntrance",
                "OldFloor",
                new BigDecimal("0.0"),
                new BigDecimal("0.0"),
                false,
                OffsetDateTime.now().minusDays(1),
                OffsetDateTime.now().minusDays(1),
                null
        );

        DeliveryAddressRequest request = new DeliveryAddressRequest(
                "NewCity",
                "NewStreet",
                "NewBuilding",
                "NewApartment",
                "NewEntrance",
                "NewFloor",
                new BigDecimal("10.0"),
                new BigDecimal("20.0")
        );

        DeliveryAddressEntity updated = mapper.updateEntity(request, existing);

        // unchanged fields
        assertEquals(5L, updated.getId());
        assertEquals(false, updated.getDefaultAddress());

        // updated fields
        assertEquals(request.city(), updated.getCity());
        assertEquals(request.street(), updated.getStreet());
        assertEquals(request.building(), updated.getBuilding());
        assertEquals(request.apartment(), updated.getApartment());
        assertEquals(request.entrance(), updated.getEntrance());
        assertEquals(request.floor(), updated.getFloor());
        assertEquals(request.latitude(), updated.getLatitude());
        assertEquals(request.longitude(), updated.getLongitude());
    }
    
    
}