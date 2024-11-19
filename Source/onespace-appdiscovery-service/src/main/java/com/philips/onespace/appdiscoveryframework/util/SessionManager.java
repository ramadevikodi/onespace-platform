/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: SessionManager.java
 */

package com.philips.onespace.appdiscoveryframework.util;

import static com.philips.onespace.util.Constants.SEMICOLON;
import static com.philips.onespace.util.Constants.SESSION_COOKIE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.philips.onespace.dto.Session;

@Component
public class SessionManager {

    @Autowired
    private CookieManager cookieManager;

    public HttpHeaders createSession(String accessToken, String refreshToken, Long expiryInSeconds) {
        String cookieValue = accessToken + SEMICOLON + refreshToken;
        ResponseCookie sessionCookie = cookieManager.createCookie(SESSION_COOKIE, cookieValue, expiryInSeconds);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, sessionCookie.toString());
        return headers;
    }

    public HttpHeaders invalidateSession(Long expiryInSeconds) {
        ResponseCookie tokenCookie = cookieManager.createCookie(SESSION_COOKIE, "", expiryInSeconds);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        return headers;
    }
    
    public Session getSession(String sessionCookie) {
        String[] sessionTokens = cookieManager.decryptAndSplitSessionCookie(sessionCookie);
        Session session = Session.builder()
        .accessToken(getTokenSafely(sessionTokens, 0, "refresh token"))
        .refreshToken(getTokenSafely(sessionTokens, 1, "refresh token"))
        .build();
        return session;
    }
    
    private String getTokenSafely(String[] tokens, int index, String tokenName) {
        if (tokens.length > index) {
            return tokens[index];
        } else {
            throw new IllegalArgumentException("Invalid session cookie format: missing " + tokenName);
        }
    }
}
