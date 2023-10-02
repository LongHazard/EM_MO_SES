package com.ncsgroup.ems.exception.vehicle.card;

import com.ncsgroup.ems.exception.base.ConflictException;

public class VehicleCardCodeAlreadyExistException extends ConflictException {
  public VehicleCardCodeAlreadyExistException(){
    setCode("com.ncsgroup.ems.exception.vehicle.vehiclecard.VehicleCardCodeAlreadyExistException");
  }
}
