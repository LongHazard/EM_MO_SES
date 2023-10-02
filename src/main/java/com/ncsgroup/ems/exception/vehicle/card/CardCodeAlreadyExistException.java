package com.ncsgroup.ems.exception.vehicle.card;

import com.ncsgroup.ems.exception.base.ConflictException;

public class CardCodeAlreadyExistException extends ConflictException {
  public CardCodeAlreadyExistException(){
    setCode("com.ncsgroup.ems.exception.vehicle.vehiclecard.CardCodeAlreadyExistException");
  }
}
