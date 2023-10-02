package com.ncsgroup.ems.exception.person;

import com.ncsgroup.ems.exception.base.BadRequestException;

public class PhoneNumberAlreadyExistException extends BadRequestException {

  public PhoneNumberAlreadyExistException() {
    setCode("com.ncsgroup.ems.exception.person.PhoneNumberAlreadyExistException");
  }
}
