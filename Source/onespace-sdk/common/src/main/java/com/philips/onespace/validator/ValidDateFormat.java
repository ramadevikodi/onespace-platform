package com.philips.onespace.validator;

import com.philips.onespace.validation.DateFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.philips.onespace.util.ErrorMessages.INVALID_DATE_FORMAT;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateFormatValidator.class)
public @interface ValidDateFormat {
    String message() default INVALID_DATE_FORMAT;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
