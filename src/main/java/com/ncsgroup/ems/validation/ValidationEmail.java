package com.ncsgroup.ems.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import static com.ncsgroup.ems.constanst.EMSConstants.MessageValidation.INVALID_EMAIL;


@Constraint(validatedBy = ValidationEmail.EmailValidation.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface ValidationEmail {

  String message() default INVALID_EMAIL;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class EmailValidation implements ConstraintValidator<ValidationEmail, String> {

    @Override
    public void initialize(ValidationEmail constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
      if (Objects.isNull(email) || email.isEmpty()) return true;
      String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
      return email.matches(emailRegex);
    }
  }
}


