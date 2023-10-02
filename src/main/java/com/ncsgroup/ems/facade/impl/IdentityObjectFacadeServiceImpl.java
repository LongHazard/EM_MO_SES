package com.ncsgroup.ems.facade.impl;

import com.ncsgroup.ems.dto.request.identity.AddIdentityObjectRequest;
import com.ncsgroup.ems.dto.request.identity.PersonVehicleRequest;
import com.ncsgroup.ems.facade.IdentityObjectFacadeService;
import com.ncsgroup.ems.service.identity.IdentityObjectService;
import com.ncsgroup.ems.service.person.PersonService;
import com.ncsgroup.ems.service.vehicle.VehicleService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdentityObjectFacadeServiceImpl implements IdentityObjectFacadeService {
  private final IdentityObjectService identityObjectService;

  private final PersonService personService;

  private final VehicleService vehicleService;

  public IdentityObjectFacadeServiceImpl(IdentityObjectService identityObjectService, PersonService personService, VehicleService vehicleService) {
    this.identityObjectService = identityObjectService;
    this.personService = personService;
    this.vehicleService = vehicleService;
  }
  @Override
  public void insert(AddIdentityObjectRequest request) {
    log.info("(insert) request:{}", request);

    for (PersonVehicleRequest personVehicleRequest : request.getPersonVehicles()) {
      personService.checkNotFound(personVehicleRequest.getPersonId());
      vehicleService.checkNotFound(personVehicleRequest.getVehicleIds());
    }

    identityObjectService.insert(request);
  }
}
