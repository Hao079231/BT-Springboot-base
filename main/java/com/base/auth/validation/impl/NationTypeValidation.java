package com.base.auth.validation.impl;


import com.base.auth.constant.UserBaseConstant;
import com.base.auth.validation.NationType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NationTypeValidation implements ConstraintValidator<NationType,Integer> {

    private boolean allowNull;

    @Override
    public void initialize(NationType constraintAnnotation) { allowNull = constraintAnnotation.allowNull(); }

    @Override
    public boolean isValid(Integer type, ConstraintValidatorContext constraintValidatorContext) {
        if(type == null && allowNull) {
            return true;
        }
        if(!Objects.equals(type, UserBaseConstant.NATION_TYPE_PROVINCE) &&
            !Objects.equals(type, UserBaseConstant.NATION_TYPE_DISTRICT) &&
            !Objects.equals(type, UserBaseConstant.NATION_TYPE_COMMUNE)) {
            return false;
        }
        return true;
    }
}
