package com.fds.userservice.servise.user;

import com.fasterxml.uuid.Generators;
import com.fds.userservice.dto.user.*;
import com.fds.userservice.entity.UserEntity;
import com.fds.userservice.enums.Role;
import com.fds.userservice.exceptions.NoSuchEntityException;
import com.fds.userservice.mappers.UserMapper;
import com.fds.userservice.repository.CustomerRepository;
import com.fds.userservice.servise.identity.IdentityProviderCommunicator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CustomerRepository customerRepository;
    private final UserMapper userMapper;
    private final IdentityProviderCommunicator identityProvider;

    @Override
    public Page<UserResponse> findAllUsers(Pageable pageable) {
        Page<UserEntity> customers = customerRepository.findAllByActiveTrue(pageable);
        
        return customers.map(userMapper::entityToDto);
    }

    @Override
    public UserResponse findUserById(UUID id) {
        UserEntity customer = getCustomer(id);

        return userMapper.entityToDto(customer);
    }

    @Override
    @Transactional
    public UserResponseWithPassword registerUser(UserRegistrationRequest request) {
        UserEntity customer = userMapper.registrationDtoToEntity(request);
        customer.setId(Generators.timeBasedEpochGenerator().generate());

        customer = customerRepository.save(customer);
        UserResponseWithPassword response =
                userMapper.entityToDtoWithPassword(customer, generateSecurePassword());

        identityProvider.createUser(response);

        return response;
    }

    @Override
    public UserResponse saveToDatabase(UserCreationRequest request) {
        UserEntity saved = customerRepository.save(userMapper.creationDtoToEntity(request));
        return userMapper.entityToDto(saved);
    }

    @Override
    public UserResponse updateUser(UUID id, UserPublicUpdateRequest request) {
        UserEntity customer = getCustomer(id);

        UserEntity updatedEntity = userMapper.updateEntity(request, customer);
        UserEntity saved = customerRepository.save(updatedEntity);

        return userMapper.entityToDto(saved);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UserAdminUpdateRequest request) {
        UserEntity customer = getCustomer(id);

        UserEntity updatedEntity = userMapper.updateEntity(request, customer);
        UserEntity saved = customerRepository.save(updatedEntity);

        identityProvider.updateEmail(id, request.email());

        return userMapper.entityToDto(saved);
    }

    @Override
    public void deleteUser(UUID id) {
        UserEntity customer = getCustomer(id);
        customer.setActive(false);
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public UserResponse updateRole(UUID id, String role) {
        try {
            Role newRole = Role.valueOf(role);
            UserEntity customer = getCustomer(id);
            Role oldRole = customer.getRole();
            customer.setRole(newRole);
            UserEntity saved = customerRepository.save(customer);
            identityProvider.updateRole(id, oldRole, newRole);
            return userMapper.entityToDto(saved);
        } catch (IllegalArgumentException e) {
            throw new NoSuchEntityException("role", role);
        }
    }

    private UserEntity getCustomer(UUID id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new NoSuchEntityException("customer", id)
        );
    }

    private String generateSecurePassword() {
        byte[] randomBytes = new byte[8];
        new SecureRandom().nextBytes(randomBytes);

        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(randomBytes)
                .substring(0, 10);
    }
}
