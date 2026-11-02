package com.fds.userservice.mappers;

import com.fds.userservice.dto.user.*;
import com.fds.userservice.entity.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {DeliveryAddressMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse entityToDto(UserEntity customer);

    List<UserResponse> entityToDto(List<UserEntity> customers);

    @Mapping(target = "active", constant = "true")
    @Mapping(target = "role", constant = "CUSTOMER")
    UserEntity creationDtoToEntity(UserCreationRequest dto);

    @Mapping(target = "active", constant = "true")
    UserEntity registrationDtoToEntity(UserRegistrationRequest dto);

    @Mapping(target = "id" , ignore = true)
    UserEntity updateEntity(UserPublicUpdateRequest dto, @MappingTarget UserEntity customer);

    @Mapping(target = "id" , ignore = true)
    UserEntity updateEntity(UserAdminUpdateRequest dto, @MappingTarget UserEntity customer);

    @Mapping(source = "password", target = "password")
    UserResponseWithPassword entityToDtoWithPassword(UserEntity customer, String password);
}
