package com.base.auth.validation;

import com.base.auth.validation.impl.CustomerAddressTypeValidation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomerAddressTypeValidation.class)
@Documented
public @interface CustomerAddressType {
  boolean allowNull() default false;
  String message() default  "Customer address type invalid.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
