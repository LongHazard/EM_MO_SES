package com.ncsgroup.ems.exception.person.card;

import com.ncsgroup.ems.exception.base.BadRequestException;

public class CardCodeAlreadyExistException extends BadRequestException {

  public CardCodeAlreadyExistException() {
    setCode("com.ncsgroup.ems.exception.person.personcard.CardCodeAlreadyExist");
  }
}
