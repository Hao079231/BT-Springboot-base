package com.base.auth.validation;


import com.base.auth.validation.impl.NationTypeValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NationTypeValidation.class)
@Documented
public @interface NationType {
    boolean allowNull() default false;
    String message() default  "Type invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
