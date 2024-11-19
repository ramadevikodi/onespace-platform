/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NotNull.java
 */

package com.philips.onespace.validator;


import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.philips.onespace.validation.NullCheckValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = NullCheckValidator.class)
public @interface NotNull {
    String message() default "{NotBlankIfAnotherFieldHasValue.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
