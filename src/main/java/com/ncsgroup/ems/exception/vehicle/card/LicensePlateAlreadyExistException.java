package com.ncsgroup.ems.exception.vehicle.card;

import com.ncsgroup.ems.exception.base.ConflictException;

public class LicensePlateAlreadyExistException extends ConflictException {
  public LicensePlateAlreadyExistException(){
    setCode("com.ncsgroup.ems.exception.vehicle.card.LicensePlateAlreadyExistException");
  }
}
