package com.ncsgroup.ems.exception.person;

import com.ncsgroup.ems.exception.base.BadRequestException;

public class EmailAlreadyExistException extends BadRequestException {

  public EmailAlreadyExistException() {
    setCode("com.ncsgroup.ems.exception.person.EmailAlreadyExistException");
  }
}
