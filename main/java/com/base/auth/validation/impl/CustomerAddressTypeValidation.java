package com.base.auth.validation.impl;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.validation.CustomerAddressType;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomerAddressTypeValidation implements
    ConstraintValidator<CustomerAddressType ,Integer> {

  private boolean allowNull;

  @Override
  public void initialize(CustomerAddressType constraintAnnotation) {
    allowNull = constraintAnnotation.allowNull();
  }

  @Override
  public boolean isValid(Integer type, ConstraintValidatorContext constraintValidatorContext) {
    if (type == null && allowNull){
      return true;
    }
    if (!Objects.equals(type, UserBaseConstant.CUSTOMER_ADDRESS_HOME) &&
        !Objects.equals(type, UserBaseConstant.CUSTOMER_ADDRESS_OFFICE)){
      return false;
    }
    return true;
  }
}
