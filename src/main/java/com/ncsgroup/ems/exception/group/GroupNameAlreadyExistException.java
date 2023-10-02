package com.ncsgroup.ems.exception.group;

import com.ncsgroup.ems.exception.base.ConflictException;

public class GroupNameAlreadyExistException extends ConflictException {
  public GroupNameAlreadyExistException(){
    setCode("com.ncsgroup.ems.exception.group.GroupNameAlreadyExistException");
  }
}
