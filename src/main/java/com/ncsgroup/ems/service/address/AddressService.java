package com.ncsgroup.ems.service.address;

import com.ncsgroup.ems.dto.request.address.AddressOfVehicleRequest;
import com.ncsgroup.ems.dto.request.address.AddressRequest;
import com.ncsgroup.ems.dto.response.address.AddressResponse;
import com.ncsgroup.ems.entity.address.Address;
import com.ncsgroup.ems.service.base.BaseService;

public interface AddressService extends BaseService<Address> {
  Address createOfPersonCard(AddressRequest request);

  Address createOfVehicleCard(AddressOfVehicleRequest request);
  Long getId(Long personCardId, String type);

  AddressResponse detail(Long id);

  AddressResponse getAddressOfPersonCard(long personCardId, String type);

  Address create(AddressRequest request, Long personCardId, String type);

  Address update(Long id, AddressRequest request, Long personCardId, String type);
}
