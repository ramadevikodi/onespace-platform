/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AuthTokenFilter.java
 */

package com.philips.onespace.appdiscoveryframework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.onespace.appdiscoveryframework.util.CookieManager;
import com.philips.onespace.appdiscoveryframework.util.IAMUtil;
import com.philips.onespace.dto.ErrorResponse;
import com.philips.onespace.dto.SecurityContextDetail;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.util.DateUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.philips.onespace.logging.LoggingAspect.logData;
import static com.philips.onespace.util.Constants.*;
import static com.philips.onespace.util.ErrorConstants.*;
import static com.philips.onespace.util.ErrorMessages.SESSION_COOKIE_AND_TOKEN_NOT_FOUND;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private IAMUtil iamUtil;
    @Autowired
    private CookieManager cookieManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        Optional<Cookie> sessionCookie = getCookie(request, SESSION_COOKIE);
        boolean isCookiePresent = sessionCookie.isPresent();
        if(!isCookiePresent && !isBearerTokenPresent(request, response)) {
            createErrorResponse(response, HttpStatus.FORBIDDEN.value(), SESSION_COOKIE_AND_TOKEN_NOT_FOUND);
            return;
        }
        try {
            String token;
            IntrospectionResponse introspectionResponse = null;
            if(isCookiePresent) {
                token = cookieManager.getAccessToken(sessionCookie.get().getValue());
                introspectionResponse = iamUtil.introspectToken(token);
                String refreshToken = cookieManager.getRefreshToken(sessionCookie.get().getValue());
                // If token is about to expire, refresh token automatically
                if(iamUtil.checkIfTokenIsExpiring(introspectionResponse.getExp())) {
                    com.philips.onespace.model.TokenResponse tokenResponse = iamUtil.exchangeForToken(REFRESH_TOKEN, refreshToken, null);
                    introspectionResponse = iamUtil.introspectToken(tokenResponse.getAccess_token());
                    ResponseCookie responseTokenCookie = cookieManager.createCookie(SESSION_COOKIE, tokenResponse.getAccess_token() + SEMICOLON + refreshToken, tokenResponse.getExpires_in());
                    response.addHeader(HttpHeaders.SET_COOKIE, responseTokenCookie.toString());
                }
            }
            else {
                token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
                introspectionResponse = iamUtil.introspectToken(token);
            }

            setSecurityContext(request, introspectionResponse);
            filterChain.doFilter(request, response);
        } catch (IOException | RuntimeException | ServletException | InvalidTokenException ex) {
            createErrorResponse(response, HttpStatus.FORBIDDEN.value(), ex.getMessage());
        } 
    }

    private static boolean isBearerTokenPresent(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return false;
        }
        return true;
    }

    private static void setSecurityContext(HttpServletRequest request, IntrospectionResponse introspectionResponse) {
        SecurityContextDetail contextDetail = buildSecurityContext(introspectionResponse);
        Set<SimpleGrantedAuthority> authorities = buildAuthorities(introspectionResponse);
        var authenticationToken = new UsernamePasswordAuthenticationToken(contextDetail, null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private static Set<SimpleGrantedAuthority> buildAuthorities(IntrospectionResponse introspectionResponse) {
        Optional<com.philips.onespace.model.Organization> userOrgWithEPOPermissions = introspectionResponse.getOrganizations().getOrganizationList()
                .stream()
                .filter(o -> o.getPermissions().stream().anyMatch(p -> p.startsWith("EPO_")))
                .findFirst();
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if(userOrgWithEPOPermissions.isPresent()) {
            authorities.addAll(userOrgWithEPOPermissions.get().getPermissions().stream().map(p -> new SimpleGrantedAuthority("ROLE_" + p)).collect(Collectors.toList()));
        }
        return authorities;
    }

    private static SecurityContextDetail buildSecurityContext(IntrospectionResponse introspectionResponse) {
        return SecurityContextDetail
                .builder()
                .userId(introspectionResponse.getSub())
                .userName(introspectionResponse.getUsername())
                .managingOrganization(introspectionResponse.getOrganizations().getManagingOrganization())
                .build();
    }

    private static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        if(request.getCookies() != null) {
            return Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(name)).findFirst();
        }
        return Optional.empty();
    }

    private static void createErrorResponse(HttpServletResponse response, int httpStatus, String message) throws IOException {
    	try {
    		response.resetBuffer();
            response.setStatus(httpStatus);
            response.setHeader("Content-Type", "application/json");
            ErrorResponse errorResponse = new ErrorResponse();
    		ErrorResponse.Issue issue = new ErrorResponse.Issue();
    		issue.setSource(ERROR_SOURCE);
    		issue.setCode(BAD_REQUEST);
    		issue.setMessage(message);
    		issue.setCategory(CLIENT_ERROR);
    		issue.setTimestamp(DateUtil.getCurrentDateUTC());
    		errorResponse.setIssue(Collections.singletonList(issue));
            response.getOutputStream().print(new ObjectMapper().writeValueAsString(errorResponse));
            response.flushBuffer(); // marks response as committed -- if we don't do this the request will go through normally!
    	} catch (Exception expObj) {
    		logData("Exception: createErrorResponse, Exception Details: ", expObj);
		}
    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        List<RequestMatcher> ignoredPaths= new ArrayList<>();
        ignoredPaths.add(new AntPathRequestMatcher(SESSION_API_PATH));
        ignoredPaths.add(new AntPathRequestMatcher(ERROR_PATH));
        return ignoredPaths.stream()
                .anyMatch(matcher -> matcher.matches(request));
    }
}
