package com.ncsgroup.ems.service.identity;

import com.ncsgroup.ems.dto.request.identity.AddIdentityObjectRequest;
import com.ncsgroup.ems.dto.request.person.AddPersonToVehicle;

public interface IdentityObjectService {
  void create(AddPersonToVehicle request);

  void update(Long vehicleId, Long personId);

  void insert(Long vehicleId, Long personId, Integer type);

  void insert(AddIdentityObjectRequest request);
}
