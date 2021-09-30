package com.mpakbaz.accountManager.http.validators;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Pattern(regexp = "^(USD|EUR)$", message = "invalid currency(should be USD or EUR)")
public @interface Currency {
    String message() default "{invalid.date}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
