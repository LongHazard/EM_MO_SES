package com.ncsgroup.ems.exception.vehicle.card;

import com.ncsgroup.ems.exception.base.NotFoundException;

public class VehicleCardNotFoundException extends NotFoundException {
  public VehicleCardNotFoundException(){
    setCode("com.ncsgroup.ems.exception.vehicle.vehiclecard.VehicleCardNotFoundException");
  }
}
