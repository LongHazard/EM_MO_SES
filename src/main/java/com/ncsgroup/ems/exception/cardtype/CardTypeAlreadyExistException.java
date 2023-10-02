package com.ncsgroup.ems.exception.cardtype;

import com.ncsgroup.ems.exception.base.ConflictException;

public class CardTypeAlreadyExistException extends ConflictException {
  public CardTypeAlreadyExistException() {
    setCode("com.ncsgroup.ems.exception.cardtype.CardTypeAlreadyExistException");
  }
}
