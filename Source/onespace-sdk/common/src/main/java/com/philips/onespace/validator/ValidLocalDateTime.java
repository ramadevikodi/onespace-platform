package com.philips.onespace.validator;

import com.philips.onespace.validation.LocalDateTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateTimeValidator.class)
public @interface ValidLocalDateTime {
    String message() default "Date cannot be in the past";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
