package com.philips.onespace.validation;


import com.philips.onespace.util.ErrorMessages;
import com.philips.onespace.validator.ValidLocalDateTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;

import java.time.ZonedDateTime;

public class LocalDateTimeValidator implements ConstraintValidator<ValidLocalDateTime, ZonedDateTime> {

    @Override
    public void initialize(ValidLocalDateTime constraintAnnotation) {
    }
    @SneakyThrows
    @Override
    public boolean isValid(ZonedDateTime value, ConstraintValidatorContext context) {
        if (value == null || value.isBefore(ZonedDateTime.now()) ) {
            throw new BadRequestException(ErrorMessages.ERR_PAST_DATE_EXPIRY);
        }

        return (value.isEqual(ZonedDateTime.now()) || value.isAfter(ZonedDateTime.now()));

    }

   }
