package com.ncsgroup.ems.exception.color;

import com.ncsgroup.ems.exception.base.BadRequestException;

public class ColorAlreadyExistException extends BadRequestException {
  public ColorAlreadyExistException() {
    setCode("com.ncsgroup.ems.exception.color.ColorAlreadyExistException");
  }
}
