package com.fds.userservice.mapper;

import com.fds.userservice.dto.user.*;
import com.fds.userservice.entity.UserEntity;
import com.fds.userservice.entity.DeliveryAddressEntity;
import com.fds.userservice.enums.Role;
import com.fds.userservice.mappers.UserMapper;
import com.fds.userservice.mappers.UserMapperImpl;
import com.fds.userservice.mappers.DeliveryAddressMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        UserMapperImpl.class,
        DeliveryAddressMapperImpl.class,
})
class UserEntityMapperTest {

    @Autowired
    private UserMapper userMapper;


    private UserEntity customer;
    private UserEntity secondCustomer;
    private List<UserEntity> customersList;

    @BeforeEach
    void setUp() {
        // First customer setup (previously in @BeforeAll)
        DeliveryAddressEntity address = new DeliveryAddressEntity(
                1L,
                "Winterfell",
                "Main courtyard",
                "Tallest tower",
                "6",
                "1",
                "3",
                new BigDecimal("65.4545455"),
                new BigDecimal("85.6452115"),
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                null
        );

        customer = new UserEntity();
        customer.setId(UUID.randomUUID());
        customer.setEmail("arya.stark@westeros.com");
        customer.setFirstName("Arya");
        customer.setLastName("Stark");
        customer.setPhoneNumber("123-456-7890");
        customer.setActive(true);
        customer.setRole(Role.CUSTOMER);
        customer.setCreatedAt(OffsetDateTime.now().minusDays(1));
        customer.setUpdatedAt(OffsetDateTime.now());
        customer.getAddresses().add(address);

        // Second customer setup
        DeliveryAddressEntity address2 = new DeliveryAddressEntity(
                1L,
                "King's Landing",
                "Red Keep",
                "Throne Room",
                "10",
                "2",
                "5",
                new BigDecimal("120.1234567"),
                new BigDecimal("200.7654321"),
                false,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                null
        );

        secondCustomer = new UserEntity();
        secondCustomer.setId(UUID.randomUUID());
        secondCustomer.setEmail("jon.snow@westeros.com");
        secondCustomer.setFirstName("Jon");
        secondCustomer.setLastName("Snow");
        secondCustomer.setPhoneNumber("555-123-4567");
        secondCustomer.setActive(false);
        secondCustomer.setRole(Role.ADMIN);
        secondCustomer.setCreatedAt(OffsetDateTime.now().minusDays(2));
        secondCustomer.setUpdatedAt(OffsetDateTime.now().minusHours(5));
        secondCustomer.getAddresses().add(address2);

        customersList = List.of(customer, secondCustomer);
    }

    @Test
    @DisplayName("Customer should be mapped correctly")
    void entityShouldBeMappedCorrectly() {
        //Arrange

        //Act
        UserResponse customerDto = userMapper.entityToDto(customer);

        //Assert
        assertNotNull(customerDto);
        assertEquals(customer.getId(), customerDto.id());
        assertEquals(customer.getEmail(), customerDto.email());
        assertEquals(customer.getFirstName(), customerDto.firstName());
        assertEquals(customer.getLastName(), customerDto.lastName());
        assertEquals(customer.getPhoneNumber(), customerDto.phoneNumber());
        assertEquals(customer.isActive(), customerDto.active());
        assertEquals(customer.getRole(), customerDto.role());
        assertEquals(customer.getCreatedAt(), customerDto.createdAt());
        assertEquals(customer.getUpdatedAt(), customerDto.updatedAt());
        assertEquals(customer.getAddresses().size(), customerDto.addresses().size());
    }

    @Test
    @DisplayName("CustomerList should be mapped correctly")
    void entityListShouldBeMappedCorrectly() {
        // Arrange

        // Act
        List<UserResponse> dtoList = userMapper.entityToDto(customersList);

        // Assert
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());

        UserResponse dto1 = dtoList.get(0);
        assertEquals(customer.getId(), dto1.id());
        assertEquals(customer.getEmail(), dto1.email());
        assertEquals(customer.getRole(), dto1.role());
        assertEquals(customer.getAddresses().size(), dto1.addresses().size());

        UserResponse dto2 = dtoList.get(1);
        assertEquals(secondCustomer.getId(), dto2.id());
        assertEquals(secondCustomer.getEmail(), dto2.email());
        assertEquals(secondCustomer.getRole(), dto2.role());
        assertEquals(secondCustomer.getAddresses().size(), dto2.addresses().size());
        assertEquals(secondCustomer.getAddresses().getFirst().getBuilding(),
                dto2.addresses().getFirst().building());
    }

    @Test
    @DisplayName("CustomerCreationDto should be mapped to Customer entity correctly")
    void creationDtoToEntityShouldBeMappedCorrectly() {
        // Arrange
        UserCreationRequest creationDto = new UserCreationRequest(
                UUID.randomUUID(),
                "brienne.of.tarth@westeros.com"
        );

        // Act
        UserEntity result = userMapper.creationDtoToEntity(creationDto);

        // Assert
        assertNotNull(result);
        assertEquals(creationDto.id(), result.getId());
        assertEquals(creationDto.email(), result.getEmail());
        assertEquals(Role.CUSTOMER, result.getRole());

        assertTrue(result.isActive());
        assertNull(result.getPhoneNumber());
        assertNotNull(result.getAddresses());
        assertTrue(result.getAddresses().isEmpty());
    }

    @Test
    @DisplayName("CustomerPublicUpdateDto should update existing Customer entity correctly")
    void updatePublicRequestTest() {
        // Arrange
        UserPublicUpdateRequest updateDto = new UserPublicUpdateRequest(
                "Sansa",
                "Stark",
                "987-654-3210"
        );

        UUID originalId = customer.getId();
        String originalEmail = customer.getEmail();
        Role originalRole = customer.getRole();
        boolean originalActive = customer.isActive();
        var originalAddresses = customer.getAddresses();

        // Act
        UserEntity updated = userMapper.updateEntity(updateDto, customer);

        // Assert
        assertNotNull(updated);
        assertEquals(originalId, updated.getId());
        assertEquals(originalEmail, updated.getEmail());
        assertEquals(originalRole, updated.getRole());
        assertEquals(originalActive, updated.isActive());
        assertEquals(originalAddresses, updated.getAddresses());

        assertEquals(updateDto.firstName(), updated.getFirstName());
        assertEquals(updateDto.lastName(), updated.getLastName());
        assertEquals(updateDto.phoneNumber(), updated.getPhoneNumber());
    }

    @Test
    @DisplayName("CustomerRegistrationDto should be mapped to Customer entity correctly")
    void registrationDtoToEntityShouldBeMappedCorrectly() {
        // Arrange
        UserRegistrationRequest registrationDto = new UserRegistrationRequest(
                "samwell.tarly@westeros.com",
                "ADMIN",
                "Samwell",
                "Tarly",
                "111-222-3333"
        );

        // Act
        UserEntity result = userMapper.registrationDtoToEntity(registrationDto);

        // Assert
        assertNotNull(result);
        assertEquals(registrationDto.email(), result.getEmail());
        assertEquals(registrationDto.firstName(), result.getFirstName());
        assertEquals(registrationDto.lastName(), result.getLastName());
        assertEquals(registrationDto.phoneNumber(), result.getPhoneNumber());
        assertEquals(Role.ADMIN, result.getRole());

        // The mapper sets active to true by default
        assertTrue(result.isActive());

        // No ID or role is set by the mapper for registration DTO
        assertNull(result.getId());

        // Addresses collection should be initialized and empty
        assertNotNull(result.getAddresses());
        assertTrue(result.getAddresses().isEmpty());
    }

    @Test
    @DisplayName("CustomerAdminUpdateDto should update existing Customer entity correctly")
    void updateAdminRequestTest() {
        // Arrange
        UserAdminUpdateRequest adminDto = new UserAdminUpdateRequest(
                "new.email@westeros.com",
                "Sansa",
                "Stark",
                "987-654-3210"
        );

        UUID originalId = customer.getId();
        Role originalRole = customer.getRole();
        boolean originalActive = customer.isActive();
        var originalAddresses = customer.getAddresses();

        // Act
        UserEntity updated = userMapper.updateEntity(adminDto, customer);

        // Assert
        assertNotNull(updated);

        assertEquals(originalId, updated.getId());
        assertEquals(originalRole, updated.getRole());
        assertEquals(originalActive, updated.isActive());
        assertEquals(originalAddresses, updated.getAddresses());

        assertEquals(adminDto.email(), updated.getEmail());
        assertEquals(adminDto.firstName(), updated.getFirstName());
        assertEquals(adminDto.lastName(), updated.getLastName());
        assertEquals(adminDto.phoneNumber(), updated.getPhoneNumber());
    }

    
}