package com.ncsgroup.ems.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.Objects;

import static com.ncsgroup.ems.constanst.EMSConstants.MessageValidation.INVALID_PHONE_NUMBER;

@Constraint(validatedBy = ValidationPhoneNumber.PhoneNumberValidation.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface ValidationPhoneNumber {

  String message() default INVALID_PHONE_NUMBER;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class PhoneNumberValidation implements ConstraintValidator<ValidationPhoneNumber, String> {

    @Override
    public void initialize(ValidationPhoneNumber constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
      if (Objects.isNull(phoneNumber) || phoneNumber.isEmpty()) return true;
      String phoneNumberRegex = "(84|0[3|5|7|8|9])+([0-9]{8})\\b";
      return phoneNumber.matches(phoneNumberRegex);
    }
  }

}

