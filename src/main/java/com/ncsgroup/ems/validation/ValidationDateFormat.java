package com.ncsgroup.ems.validation;


import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.Objects;

import static com.ncsgroup.ems.constanst.EMSConstants.MessageValidation.INVALID_DATE_FORMAT;

@Constraint(validatedBy = ValidationDateFormat.DateFormatValidation.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface ValidationDateFormat {

  String message() default INVALID_DATE_FORMAT;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class DateFormatValidation implements ConstraintValidator<ValidationDateFormat, String> {

    @Override
    public void initialize(ValidationDateFormat constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
      String dateFormatRegex = "^\\d{2}/\\d{2}/\\d{4}$";
      if (Objects.isNull(date)) return true;
      return date.matches(dateFormatRegex);
    }
  }
}

