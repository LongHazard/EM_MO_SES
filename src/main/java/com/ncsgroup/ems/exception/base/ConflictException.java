package com.ncsgroup.ems.exception.base;


import static com.ncsgroup.ems.constanst.ExceptionConstants.CONFLICT;

public class ConflictException extends BaseException {
  public ConflictException(String id, String objectName) {
    setStatus(CONFLICT);
    setCode("com.ncsgroup.core.authentication.exception.base.ConflictException");
    addParam("id", id);
    addParam("objectName", objectName);
  }

  public ConflictException(){
    setStatus(CONFLICT);
    setCode("com.ncsgroup.core.authentication.exception.base.ConflictException");
  }
}
