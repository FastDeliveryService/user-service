package com.fds.userservice.mappers;

import com.fds.userservice.dto.address.DeliveryAddressRequest;
import com.fds.userservice.dto.address.DeliveryAddressResponse;
import com.fds.userservice.entity.DeliveryAddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryAddressMapper {

    @Mapping(target = "userId", source = "user.id")
    DeliveryAddressResponse entityToDto(DeliveryAddressEntity address);

    List<DeliveryAddressResponse> entityToDto(List<DeliveryAddressEntity> addresses);

    DeliveryAddressEntity dtoToEntity(DeliveryAddressRequest dto);

    DeliveryAddressEntity updateEntity(DeliveryAddressRequest dto, @MappingTarget DeliveryAddressEntity address);

}
