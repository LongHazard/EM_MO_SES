package com.ncsgroup.ems.exception.vehicle.brand;

import com.ncsgroup.ems.exception.base.NotFoundException;

public class VehicleBrandNotFoundException extends NotFoundException {

  public VehicleBrandNotFoundException() {
    setCode("com.ncsgroup.ems.exception.vehicle.vehiclebrand.VehicleBrandNotFoundException");
  }
}
