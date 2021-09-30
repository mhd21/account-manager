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
@Pattern(regexp = "^\\d{4}-(((0)[0-9])|((1)[0-2]))-([0-2][0-9]|(3)[0-1])$", message = "invalid date format (valid: yyyy-MM-dd)")
public @interface DateFormat {
    String message() default "{invalid.date}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
