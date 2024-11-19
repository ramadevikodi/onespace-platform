/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: InputValidator.java
 */

package com.philips.onespace.validation;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import com.philips.onespace.validator.ValidInput;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class InputValidator implements ConstraintValidator<ValidInput, String> {

    private static final String SQL_PATTERN = "(?i).*(select|insert|update|delete|drop|truncate|exec|create|alter).*$";

    private String relaxedField;
    private static final Pattern ALLOWED_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,\\.?!'\":;@#$%&*\\-+()\\[\\]{}_\\\\/]+$");

    @Override
    public void initialize(ValidInput constraintAnnotation) {
        this.relaxedField = constraintAnnotation.relaxedField();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        else if(value.matches(SQL_PATTERN)){
            return false;
        }
        return checkForSpecialCharAndJS(value, context) ;
    }
    public boolean checkForSpecialCharAndJS(String value, ConstraintValidatorContext context) {
        if (context.getDefaultConstraintMessageTemplate().equals(relaxedField)) {
            // Check for JavaScript injection patterns
            Pattern jsPattern = Pattern.compile("(?i)(<script|alert\\(|eval\\(|document\\.|window\\.)");
            if (jsPattern.matcher(value).find()) {
                return false;
            }
            // Validate input with allowed characters and disallowed patterns
            // Check allowed pattern
            if (!ALLOWED_PATTERN.matcher(value).matches()) {
                return false;
            }
            // Check for multiple '%'
            long percentCount = value.chars().filter(ch -> ch == '%').count();
            if (percentCount > 5) {
                return false;
            }
        }
        else{
            // Define a strict safelist to allow only text
            PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
            String cleanInput = policy.sanitize(value);

            // If the cleaned input is not the same as the original, it had invalid content
            return cleanInput.equals(value);
        }
        return true;
    }

}
