package com.ncsgroup.ems.exception.vehicle;

import com.ncsgroup.ems.exception.base.NotFoundException;

public class VehicleNotFoundException extends NotFoundException {
  public VehicleNotFoundException(){
    setCode("com.ncsgroup.ems.exception.vehicle.VehicleNotFoundException");
  }
}
