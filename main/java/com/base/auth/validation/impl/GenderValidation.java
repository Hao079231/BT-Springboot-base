package com.base.auth.validation.impl;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.validation.Gender;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GenderValidation implements ConstraintValidator<Gender, Integer> {

  private boolean allowNull;

  @Override
  public void initialize(Gender constraintAnnotation) {
    allowNull = constraintAnnotation.allowNull();
  }

  @Override
  public boolean isValid(Integer gender, ConstraintValidatorContext constraintValidatorContext) {
    if (gender == null && allowNull){
      return true;
    }
    if(!Objects.equals(gender, UserBaseConstant.MALE_GENDER) &&
        !Objects.equals(gender, UserBaseConstant.FEMALE_GENDER) &&
        !Objects.equals(gender, UserBaseConstant.OTHER_GENDER)) {
      return false;
    }
    return true;
  }
}
