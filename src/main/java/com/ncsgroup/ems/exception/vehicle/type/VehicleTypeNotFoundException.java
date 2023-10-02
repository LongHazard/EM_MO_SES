package com.ncsgroup.ems.exception.vehicle.type;

import com.ncsgroup.ems.exception.base.NotFoundException;

public class VehicleTypeNotFoundException extends NotFoundException {
  public VehicleTypeNotFoundException(){
    setCode("com.ncsgroup.ems.exception.vehicle_type.VehicleTypeNotFoundException");
  }
}
