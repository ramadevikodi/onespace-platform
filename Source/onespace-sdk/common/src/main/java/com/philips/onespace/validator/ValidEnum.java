package com.philips.onespace.validator;

import static com.philips.onespace.util.ErrorMessages.INVALID_ENUM_VALUE;
import static java.lang.annotation.ElementType.TYPE;

import com.philips.onespace.validation.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumValidator.class)
@Target({TYPE,ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {

    Class<? extends Enum<?>> enumClass();

    String message() default INVALID_ENUM_VALUE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
