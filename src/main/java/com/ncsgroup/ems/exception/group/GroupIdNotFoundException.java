package com.ncsgroup.ems.exception.group;

import com.ncsgroup.ems.exception.base.NotFoundException;

public class GroupIdNotFoundException extends NotFoundException {
  public GroupIdNotFoundException(){
    setCode("com.ncsgroup.ems.exception.group.GroupIdNotFoundException");
  }
}
