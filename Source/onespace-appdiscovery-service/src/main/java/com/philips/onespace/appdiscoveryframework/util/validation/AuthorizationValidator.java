package com.philips.onespace.appdiscoveryframework.util.validation;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.philips.onespace.appdiscoveryframework.util.CookieManager;
import com.philips.onespace.util.ErrorMessages;

/**
 * This validator validates the presence of session cookie or token in the request.
 * Priority is given to session cookie.
 */
@Component
public class AuthorizationValidator {

    @Autowired
    private CookieManager cookieManager;

    public String validateAndGetToken(String sessionCookie, String authorization) throws BadRequestException {
        checkForCookieOrBearerToken(sessionCookie, authorization);
        return getToken(sessionCookie, authorization);
    }

    public String validateAndGetRefreshToken(String sessionCookie, String refreshToken) throws BadRequestException {
        if (sessionCookie == null && refreshToken == null) {
            throw new BadRequestException(ErrorMessages.SESSION_COOKIE_AND_TOKEN_NOT_FOUND);
        }
        if(sessionCookie != null) {
            return cookieManager.getRefreshToken(sessionCookie);
        } else {
            return refreshToken;
        }
    }

    private String getToken(String sessionCookie, String authorization) {
        String token = null;
        if (sessionCookie != null) {
            token = cookieManager.getAccessToken(sessionCookie);
        } else if (authorization != null) {
            token = authorization.substring(7);
        }
        return token;
    }

    private void checkForCookieOrBearerToken(String sessionCookie, String authorization) throws BadRequestException {
        if (sessionCookie == null && (authorization == null || !authorization.startsWith("Bearer "))) {
            throw new BadRequestException(ErrorMessages.SESSION_COOKIE_AND_TOKEN_NOT_FOUND);
        }
    }
}
