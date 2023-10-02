package com.ncsgroup.ems.facade;

import com.ncsgroup.ems.dto.request.identity.AddIdentityObjectRequest;

public interface IdentityObjectFacadeService {

//  private final IdentityObjectService identityObjectService;
//
//  private final PersonService personService;
//
//  private final VehicleService vehicleService;
//
//  public IdentityObjectFacadeService(IdentityObjectService identityObjectService, PersonService personService, VehicleService vehicleService) {
//    this.identityObjectService = identityObjectService;
//    this.personService = personService;
//    this.vehicleService = vehicleService;
//  }

  void insert(AddIdentityObjectRequest request);
}
