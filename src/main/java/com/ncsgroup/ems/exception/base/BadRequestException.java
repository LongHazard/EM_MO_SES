package com.ncsgroup.ems.exception.base;


import static com.ncsgroup.ems.constanst.ExceptionConstants.BAD_REQUEST;

public class BadRequestException extends BaseException {
  public BadRequestException() {
    setCode("com.ncsgroup.core.authentication.exception.base.BadRequestException");
    setStatus(BAD_REQUEST);
  }
}
