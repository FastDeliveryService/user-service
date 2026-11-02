package com.fds.userservice.repository;

import com.fds.userservice.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<UserEntity, UUID> {

    Page<UserEntity> findAllByActiveTrue(Pageable pageable);
}
