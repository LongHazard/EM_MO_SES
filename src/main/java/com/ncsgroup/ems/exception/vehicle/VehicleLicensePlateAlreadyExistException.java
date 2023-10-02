package com.ncsgroup.ems.exception.vehicle;

import com.ncsgroup.ems.exception.base.ConflictException;

public class VehicleLicensePlateAlreadyExistException extends ConflictException {
  public VehicleLicensePlateAlreadyExistException(){
    setCode("com.ncsgroup.ems.exception.vehicle.VehicleLicensePlateAlreadyExistException");
  }
}
