package com.philips.onespace.appdiscoveryframework.util;

import com.philips.onespace.validation.LocalDateTimeValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class LocalDateTimeValidatorTest {

    private LocalDateTimeValidator localDateTimeValidator;

    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        localDateTimeValidator = new LocalDateTimeValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    public void testFutureTime() {
        ZonedDateTime futureDate = ZonedDateTime.now().plusMinutes(10);
        assertTrue(localDateTimeValidator.isValid(futureDate, context));
    }


    @Test
    public void testPastDate() {
        ZonedDateTime pastDate = ZonedDateTime.now().minusMinutes(1);
        assertThrows(BadRequestException.class, () -> {
            localDateTimeValidator.isValid(pastDate, context);
        });
    }


    @Test
    public void testIsValidCurrentDate() {
        ZonedDateTime currentDate = ZonedDateTime.now().plusSeconds(10);
        assertTrue(localDateTimeValidator.isValid(currentDate, context));
    }

    @Test
    public void testNullDate() {
        assertThrows(BadRequestException.class, () -> {
            localDateTimeValidator.isValid(null, context);
        });

    }


}
