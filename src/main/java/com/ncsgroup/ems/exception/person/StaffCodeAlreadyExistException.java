package com.ncsgroup.ems.exception.person;

import com.ncsgroup.ems.exception.base.BadRequestException;

public class StaffCodeAlreadyExistException extends BadRequestException {

  public StaffCodeAlreadyExistException() {
    setCode("com.ncsgroup.ems.exception.person.StaffCodeAlreadyExistException");
  }
}
