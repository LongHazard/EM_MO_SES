package com.ncsgroup.ems.exception.group;

import com.ncsgroup.ems.exception.base.ConflictException;

public class SubGroupAlreadyExistParentIdException extends ConflictException {
  public SubGroupAlreadyExistParentIdException(){
    setCode("com.ncsgroup.ems.exception.group.SubGroupAlreadyExistParentIdException");
  }
}
