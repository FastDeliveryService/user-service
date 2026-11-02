package com.fds.userservice.security;

import com.fds.security.utils.SecurityUtils;
import com.fds.userservice.repository.DeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("addressSecurity")
@RequiredArgsConstructor
public class AddressSecurityExpressions {
    private final DeliveryAddressRepository repository;

    public boolean isOwner(long addressId) {

        return repository.existsByIdAndUserId(addressId, SecurityUtils.getUserId());
    }
}
