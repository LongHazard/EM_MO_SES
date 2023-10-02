package com.ncsgroup.ems.exception.cardtype;

import com.ncsgroup.ems.exception.base.NotFoundException;

public class CardTypeNotFoundException extends NotFoundException {
  public CardTypeNotFoundException(){
    setCode("com.ncsgroup.ems.exception.cardtype.CardTypeNotFoundException");
  }
}
