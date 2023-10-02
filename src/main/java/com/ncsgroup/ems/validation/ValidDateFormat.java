package com.ncsgroup.ems.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.Objects;

import static com.ncsgroup.ems.constanst.EMSConstants.MessageValidation.INVALID_DATE_FORMAT;

@Constraint(validatedBy = ValidDateFormat.DateFormatValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface ValidDateFormat {
  String message() default INVALID_DATE_FORMAT;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class DateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {
    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
      // Kiểm tra tính hợp lệ của định dạng ngày tháng năm
      String regex = "^(0[1-9]|1\\d|2\\d|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
      if (Objects.isNull(date)) return true;
      return date.matches(regex);
    }
  }
}
