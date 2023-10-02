package com.ncsgroup.ems.exception.person.card;

import com.ncsgroup.ems.exception.base.NotFoundException;

public class PersonCardNotFoundException extends NotFoundException {
  public PersonCardNotFoundException() {
    setCode("com.ncsgroup.ems.exception.person.personcard.PersonCardNotFoundException");
  }
}
