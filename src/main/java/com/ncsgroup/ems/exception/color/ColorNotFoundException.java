package com.ncsgroup.ems.exception.color;

import com.ncsgroup.ems.exception.base.NotFoundException;

public class ColorNotFoundException extends NotFoundException {
  public ColorNotFoundException() {
    setCode("com.ncsgroup.ems.exception.color.ColorNotFoundException");
  }
}
