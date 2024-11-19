package com.philips.onespace.appdiscoveryframework.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.philips.onespace.validation.InputValidator;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

class InputValidatorTest {

    private InputValidator inputValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp(){
        inputValidator = new InputValidator();
        context = mock(ConstraintValidatorContext.class);
        ConstraintViolationBuilder violationBuilder = mock(ConstraintViolationBuilder.class);

        when(context.getDefaultConstraintMessageTemplate()).thenReturn("defaultMessage");
        when(context.buildConstraintViolationWithTemplate("defaultMessage")).thenReturn(violationBuilder);
    }

    @Test
    @DisplayName("Test for Valid input")
    void testValidInput(){
        assertTrue(inputValidator.isValid("normalinput", context));
        assertTrue(inputValidator.isValid("1234567890", context));
        assertTrue(inputValidator.isValid("someinput_with-dash", context));
    }

    @Test
    @DisplayName("Test for SQL script")
    void testSqlInjectionInput() {
        assertFalse(inputValidator.isValid("1 OR 1=1", context));
        assertFalse(inputValidator.isValid("select * from emp", context));
        assertFalse(inputValidator.isValid("'; DROP TABLE users; --", context));
    }

    @Test
    @DisplayName("Test for Javascript")
    void testJavaScriptInjectionInput() {
        assertFalse(inputValidator.isValid("<script>alert('XSS')</script>", context));
        assertFalse(inputValidator.isValid("onmouseover=alert('XSS')", context));
    }

    @Test
    @DisplayName("Test for Special Characters")
    void testSpecialCharactersInput() {
        assertFalse(inputValidator.isValid("input@!$", context));
        assertFalse(inputValidator.isValid("input^&*", context));
        assertFalse(inputValidator.isValid("input<>?", context));
    }

    @Test
    @DisplayName("Test for Empty/Null input")
    void testEmptyAndNullInput() {
        assertTrue(inputValidator.isValid("", context));
        assertTrue(inputValidator.isValid(null, context));
    }

}