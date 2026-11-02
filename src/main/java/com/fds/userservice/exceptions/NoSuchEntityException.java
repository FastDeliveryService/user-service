package com.fds.userservice.exceptions;

import com.fds.userservice.servise.address.DeliveryAddressService;

import java.util.NoSuchElementException;
import java.util.UUID;

public class NoSuchEntityException extends NoSuchElementException {
    public NoSuchEntityException(String entityName,  Object value) {
        super(String.format("Entity '%s' with value '%s' does not exist.", entityName, value));
    }
}
