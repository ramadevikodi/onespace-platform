package com.philips.onespace.validation;

import com.philips.onespace.exception.EnumValidationException;
import com.philips.onespace.validator.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static com.philips.onespace.util.EnumUtil.getEnumNameByValue;
import static com.philips.onespace.util.ErrorMessages.ENUM_RETRIEVAL_ERR_CODE;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private String[] acceptedValues;

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum annotation) {
        enumClass = annotation.enumClass();
        acceptedValues = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .toArray(String[]::new);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return value == null || Arrays.asList(acceptedValues).contains(getEnumNameByValue(enumClass, value));
        } catch (IllegalArgumentException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new EnumValidationException(ENUM_RETRIEVAL_ERR_CODE + value, e);
        }
    }
}
