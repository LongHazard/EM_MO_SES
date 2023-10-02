package com.ncsgroup.ems.service.address.impl;

import com.ncsgroup.ems.dto.request.address.AddressOfVehicleRequest;
import com.ncsgroup.ems.dto.request.address.AddressRequest;
import com.ncsgroup.ems.dto.response.address.AddressResponse;
import com.ncsgroup.ems.entity.address.Address;
import com.ncsgroup.ems.repository.AddressRepository;
import com.ncsgroup.ems.service.address.AddressService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import com.ncsgroup.ems.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressServiceImpl extends BaseServiceImpl<Address> implements AddressService {

  private final AddressRepository repository;

  public AddressServiceImpl(AddressRepository repository) {
    super(repository);
    this.repository = repository;
  }

  //  @Override
  public Address create(AddressRequest request, Long personCardId, String type) {
    log.info("(createOfPersonCard) request :{}", request);

    Address address = buildAddressPreCreate(request, personCardId, type);
    super.create(address);

    return address;
  }

  @Override
  public Address update(Long id, AddressRequest request, Long personCardId, String type) {
    log.info("(update) id:{}, request:{}", id, request);

    Address address = MapperUtils.toEntity(request, Address.class);
    address.setId(id);
    log.info("address ==============================+> {}", address.getAddressDetail());
    address.setPersonCardId(personCardId);
    address.setType(type);

    Address savedAddress = update(address);
    log.info("address ========================> {}", address.getId());
    return savedAddress;
  }

  @Override
  public Address createOfPersonCard(AddressRequest request) {
    log.info("(createOfPersonCard) request :{}", request);

    Address address = MapperUtils.toEntity(request, Address.class);
    address.setId(null);

    return super.create(address);
  }

  @Override
  public Address createOfVehicleCard(AddressOfVehicleRequest request) {
    log.info("(createOfVehicleCard) request :{}", request);

    Address address = MapperUtils.toEntity(request, Address.class);
    address.setId(null);

    return super.create(address);
  }

  @Override
  public Long getId(Long personCardId, String type) {
    log.info("(getId) personCardId:{}, type:{}", personCardId, type);
    return repository.getId(personCardId, type);
  }

  @Override
  public AddressResponse detail(Long id) {
    log.info("(detail) id:{}", id);
    return repository.get(id);
  }

  @Override
  public AddressResponse getAddressOfPersonCard(long personCardId, String type) {
    return repository.getAddressOfPersonCard(personCardId, type);
  }

  private Address buildAddressPreCreate(AddressRequest request, Long personCardId, String type) {
    log.info("(buildAddressPreCreate) request :{}, personCardId: {}, type: {}", request, personCardId, type);
    Address address = MapperUtils.toEntity(request, Address.class);
    address.setId(null);
    address.setPersonCardId(personCardId);
    address.setType(type);

    return address;
  }
}
