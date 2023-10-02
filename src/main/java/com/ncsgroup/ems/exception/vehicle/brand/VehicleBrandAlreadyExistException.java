package com.ncsgroup.ems.exception.vehicle.brand;

import com.ncsgroup.ems.exception.base.ConflictException;

public class VehicleBrandAlreadyExistException extends ConflictException {

  public VehicleBrandAlreadyExistException() {
    setCode("com.ncsgroup.ems.exception.vehicle.vehiclebrand.VehicleBrandAlreadyExistException");
  }
}
