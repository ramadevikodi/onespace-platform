package com.philips.onespace.appdiscoveryframework.util.validation;

import static com.philips.onespace.util.IamConstants.BEARER;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.philips.onespace.appdiscoveryframework.util.CookieManager;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AuthorizationValidatorTest {

    @Mock
    private CookieManager cookieManager;

    @InjectMocks
    private AuthorizationValidator authorizationValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateAndGetToken_WithSessionCookie() throws BadRequestException {

        String sessionCookie = "sessionCookie";
        String authorization = null;
        String expectedToken = "accessToken";
        when(cookieManager.getAccessToken(sessionCookie)).thenReturn(expectedToken);
        String token = authorizationValidator.validateAndGetToken(sessionCookie, authorization);
        assertEquals(expectedToken, token);
        verify(cookieManager, times(1)).getAccessToken(sessionCookie);
    }

    @Test
    public void testValidateAndGetToken_WithAuthorization() throws BadRequestException {

        String sessionCookie = null;
        String authorization = BEARER +" token";
        String expectedToken = "token";

        String token = authorizationValidator.validateAndGetToken(sessionCookie, authorization);


        assertEquals(expectedToken, token);
    }

    @Test
    public void testValidateAndGetToken_ThrowsBadRequestException() {

        String sessionCookie = null;
        String authorization = null;
        assertThrows(BadRequestException.class, () -> {
            authorizationValidator.validateAndGetToken(sessionCookie, authorization);
        });
    }

    @Test
    public void testValidateAndGetRefreshToken_WithSessionCookie() throws BadRequestException {

        String sessionCookie = "sessionCookie";
        String refreshToken = null;
        String expectedToken = "refreshToken";
        when(cookieManager.getRefreshToken(sessionCookie)).thenReturn(expectedToken);
        String token = authorizationValidator.validateAndGetRefreshToken(sessionCookie, refreshToken);
        assertEquals(expectedToken, token);
        verify(cookieManager, times(1)).getRefreshToken(sessionCookie);
    }

    @Test
    public void testValidateAndGetRefreshToken_WithRefreshToken() throws BadRequestException {

        String sessionCookie = null;
        String refreshToken = "refreshToken";
        String token = authorizationValidator.validateAndGetRefreshToken(sessionCookie, refreshToken);
        assertEquals(refreshToken, token);
    }

    @Test
    public void testValidateAndGetRefreshToken_ThrowsBadRequestException() {

        String sessionCookie = null;
        String refreshToken = null;
        assertThrows(BadRequestException.class, () -> {
            authorizationValidator.validateAndGetRefreshToken(sessionCookie, refreshToken);
        });
    }
}

