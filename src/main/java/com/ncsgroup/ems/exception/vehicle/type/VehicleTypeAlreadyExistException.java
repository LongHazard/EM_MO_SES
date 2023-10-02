package com.ncsgroup.ems.exception.vehicle.type;

import com.ncsgroup.ems.exception.base.BadRequestException;

public class VehicleTypeAlreadyExistException extends BadRequestException {
  public VehicleTypeAlreadyExistException(){
    setCode("com.ncsgroup.ems.exception.vehicle_type.VehicleTypeAlreadyExistException");
  }
}
