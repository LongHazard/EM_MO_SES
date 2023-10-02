package com.ncsgroup.ems.exception.person;

import com.ncsgroup.ems.exception.base.BadRequestException;

public class PersonCardAlreadyExistException extends BadRequestException {
  public PersonCardAlreadyExistException() {
    setCode("com.ncsgroup.ems.exception.person.PersonCardAlreadyExistException");
  }
}
