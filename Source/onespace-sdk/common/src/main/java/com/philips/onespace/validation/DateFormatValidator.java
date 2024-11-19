package com.philips.onespace.validation;


import com.philips.onespace.validator.ValidDateFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.philips.onespace.util.Constants.DATE_FORMAT_WITHOUT_TIMEZONE;

public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_TIMEZONE);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            formatter.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
