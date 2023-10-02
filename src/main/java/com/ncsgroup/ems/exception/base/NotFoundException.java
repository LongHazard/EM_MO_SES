package com.ncsgroup.ems.exception.base;


import static com.ncsgroup.ems.constanst.ExceptionConstants.NOT_FOUND;

public class NotFoundException extends BaseException {
  public NotFoundException(String id, String objectName) {
    setCode("com.ncsgroup.core.authentication.exception.base.NotFoundException");
    setStatus(NOT_FOUND);
    addParam("id", id);
    addParam("objectName", objectName);
  }

  public NotFoundException() {
    setStatus(NOT_FOUND);
    setCode("com.ncsgroup.core.authentication.exception.base.NotFoundException");
  }
}
