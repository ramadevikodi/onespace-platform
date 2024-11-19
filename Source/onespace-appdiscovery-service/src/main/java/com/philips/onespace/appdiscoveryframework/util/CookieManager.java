/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: CookieManager.java
 */

package com.philips.onespace.appdiscoveryframework.util;

import static com.philips.onespace.util.Constants.SEMICOLON;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.philips.onespace.util.EncryptionUtil;

@Component
public class CookieManager {

    @Autowired
    private EncryptionUtil encryptionUtil;

    public String getAccessToken(String sessionCookie) {
        return decryptAndSplitSessionCookie(sessionCookie)[0];
    }
    
    public String getRefreshToken(String sessionCookie) {
        String[] parts = decryptAndSplitSessionCookie(sessionCookie);
        return getTokenSafely(parts, 1, "refresh token");
    }

    private String getTokenSafely(String[] tokens, int index, String tokenName) {
        if (tokens.length > index) {
            return tokens[index];
        } else {
            throw new IllegalArgumentException("Invalid session cookie format: missing " + tokenName);
        }
    }
    
    public ResponseCookie createCookie(String name, String value, Long expiryInSeconds) {
        return ResponseCookie.from(name, !StringUtils.isBlank(value) ? encryptionUtil.encrypt(value) : value )
//                .httpOnly(true) // Relaxing the HttpOnly constraint to allow JS to read encrypted cookie value
                .secure(true)
                .sameSite(Cookie.SameSite.NONE.attributeValue())
                .path("/")
                .maxAge(expiryInSeconds)
                .build();
    }
    
    public String[] decryptAndSplitSessionCookie(String sessionCookie) {
        String decryptedCookie = encryptionUtil.decrypt(sessionCookie);
        return decryptedCookie.split(SEMICOLON);
    }
}
