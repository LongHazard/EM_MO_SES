package com.ncsgroup.ems.exception.person;

import com.ncsgroup.ems.exception.base.NotFoundException;

public class PersonNotFoundException extends NotFoundException {

  public PersonNotFoundException() {
    setCode("com.ncsgroup.ems.exception.person.PersonNotFoundException");
  }
}
