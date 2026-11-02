package com.fds.userservice.servise.user;

import com.fds.userservice.dto.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    Page<UserResponse> findAllUsers(Pageable pageable);
    UserResponse findUserById(UUID id);
    UserResponse saveToDatabase(UserCreationRequest request);
    UserResponseWithPassword registerUser(UserRegistrationRequest request);
    UserResponse updateUser(UUID id, UserPublicUpdateRequest request);
    UserResponse updateUser(UUID id, UserAdminUpdateRequest request);
    void deleteUser(UUID id);
    UserResponse updateRole(UUID id, String role);
}
