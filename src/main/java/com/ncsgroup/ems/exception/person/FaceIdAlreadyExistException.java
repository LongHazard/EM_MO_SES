package com.ncsgroup.ems.exception.person;

import com.ncsgroup.ems.exception.base.BadRequestException;

public class FaceIdAlreadyExistException extends BadRequestException {

  public FaceIdAlreadyExistException() {
    setCode("com.ncsgroup.ems.exception.person.FaceIdAlreadyExistException");
  }
}
