package com.fds.userservice.servise.identity;

import com.fds.userservice.dto.user.UserResponseWithPassword;
import com.fds.userservice.entity.UserEntity;
import com.fds.userservice.enums.Role;

import java.util.UUID;

public interface IdentityProviderCommunicator {

    void createUser(UserResponseWithPassword customerToCreate);

    void updateEmail(UUID userId, String email);

    void updateRole(UUID userId,  Role oldRole, Role newRole);
}
