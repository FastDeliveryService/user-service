package com.fds.userservice.servise.address;

import com.fds.userservice.dto.address.DeliveryAddressRequest;
import com.fds.userservice.dto.address.DeliveryAddressResponse;
import com.fds.userservice.entity.UserEntity;
import com.fds.userservice.entity.DeliveryAddressEntity;
import com.fds.userservice.exceptions.NoSuchEntityException;
import com.fds.userservice.mappers.DeliveryAddressMapper;
import com.fds.userservice.repository.CustomerRepository;
import com.fds.userservice.repository.DeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    private final DeliveryAddressRepository repository;
    private final CustomerRepository customerRepository;
    private final DeliveryAddressMapper mapper;

    @Override
    public Page<DeliveryAddressResponse> getDeliveryAddresses(Pageable pageable) {
        Page<DeliveryAddressEntity> all = repository.findAll(pageable);
        return all.map(mapper::entityToDto);
    }

    @Override
    public List<DeliveryAddressResponse> getDeliveryAddresses(UUID customerId) {
        List<DeliveryAddressEntity> addresses = repository.readAllByUserId(customerId);
        return mapper.entityToDto(addresses);
    }

    @Override
    public DeliveryAddressResponse getDeliveryAddress(Long id) {
        DeliveryAddressEntity address = getAddress(id);
        return mapper.entityToDto(address);
    }

    @Override
    @Transactional
    public DeliveryAddressResponse createDeliveryAddress(UUID customerId, DeliveryAddressRequest request) {
        UserEntity user = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchEntityException("user", customerId));

        DeliveryAddressEntity address = mapper.dtoToEntity(request);

        address.setDefaultAddress(user.getAddresses().isEmpty());

        address.setUser(user);

        address = repository.saveAndFlush(address);
        return mapper.entityToDto(address);
    }

    @Override
    public DeliveryAddressResponse updateDeliveryAddress(Long id, DeliveryAddressRequest request) {
        DeliveryAddressEntity address = getAddress(id);
        address = mapper.updateEntity(request, address);

        repository.save(address);
        return mapper.entityToDto(address);
    }

    @Override
    @Transactional
    public void makeDefault(Long id) {
        DeliveryAddressEntity address = getAddress(id);

        repository.readByUserIdAndDefaultAddressTrue(address.getUser().getId())
                .ifPresent(oldDefault -> oldDefault.setDefaultAddress(false));
        repository.flush();

        address.setDefaultAddress(true);
        repository.save(address);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }


    private DeliveryAddressEntity getAddress(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("address", id));
    }
}
