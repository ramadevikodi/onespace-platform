/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AuthorizationController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import com.philips.onespace.appdiscoveryframework.util.IAMUtil;
import com.philips.onespace.appdiscoveryframework.util.SessionManager;
import com.philips.onespace.appdiscoveryframework.util.validation.AuthorizationValidator;
import com.philips.onespace.dto.ClientCredentials;
import com.philips.onespace.dto.Session;
import com.philips.onespace.exception.InvalidClientException;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.model.TokenResponse;
import com.philips.onespace.util.ErrorMessages;
import com.philips.onespace.validator.ApiVersion;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import static com.philips.onespace.util.Constants.REFRESH_TOKEN;
import static com.philips.onespace.util.Constants.SESSION_COOKIE;
import static com.philips.onespace.util.ErrorConstants.INVALID_CLIENT_ERR_CODE;
import static com.philips.onespace.util.IamConstants.*;

@RestController
@Validated
@RequestMapping("/Session")
public class AuthorizationController {
    private IAMUtil iamUtil;
    
    private SessionManager sessionManager;
    
    private AuthorizationValidator authorizationValidator;

    @Autowired
    public AuthorizationController(IAMUtil iamUtil, SessionManager sessionManager,
                                   AuthorizationValidator authorizationValidator) {
        this.iamUtil = iamUtil;
        this.sessionManager = sessionManager;
        this.authorizationValidator = authorizationValidator;
    }

    /**
	 * Save CookieOrToken.
	 *
	 * @param sessionCookie the sessionCookie
	 * @return the introspect response.
	 * @throws Exception 
	 */
    @ApiVersion("1")
    @RequestMapping(path = "/$Introspect", method = RequestMethod.POST)
    public ResponseEntity<IntrospectionResponse> introspectCookieOrToken(@CookieValue(value = SESSION_COOKIE, required = false) String sessionCookie,
                                                                         @RequestHeader(value="Authorization", required=false) String authorization) throws Exception {
        String token = authorizationValidator.validateAndGetToken(sessionCookie, authorization);
        IntrospectionResponse introspectionResponse = iamUtil.introspectToken(token);
        return new ResponseEntity<>(introspectionResponse, HttpStatus.OK);
    }

    /**
	 * Save Cookie.
	 *
	 * @return the introspect response.
	 * @throws Exception 
	 */
    @ApiVersion("1")
    @RequestMapping(path = "/Cookie", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity exchangeAuthorizationCodeWithCookie(@RequestParam MultiValueMap<String, String> paramMap) throws Exception {
        TokenResponse tokenResponse = validateAndExchangeForToken(paramMap);
        HttpHeaders headers = sessionManager.createSession(tokenResponse.getAccess_token(),
                tokenResponse.getRefresh_token(), tokenResponse.getExpires_in());
        return new ResponseEntity(headers, HttpStatus.OK);
    }

    /**
	 * Generate token.
	 *
	 * @return the token response.
	 * @throws Exception
	 */
    @ApiVersion("1")
    @RequestMapping(path = "/Token", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<TokenResponse> exchangeAuthorizationCodeWithToken(@RequestParam MultiValueMap<String, String> paramMap) throws Exception {
        TokenResponse tokenResponse = validateAndExchangeForToken(paramMap);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    /**
	 * Invalidate CookieOrToken.
	 *
	 * @return the invalidate CookieOrToken status.
	 * @throws Exception
	 */
    @ApiVersion("1")
    @RequestMapping(path = "/$Logout", method = RequestMethod.POST)
    public ResponseEntity invalidateCookieOrToken(@CookieValue(value = SESSION_COOKIE, required = false) String sessionCookie,
                                                  @RequestHeader(value="Authorization", required=false) String authorization) throws Exception {
        String token = authorizationValidator.validateAndGetToken(sessionCookie, authorization);
        iamUtil.revokeToken(token);
        if(sessionCookie != null) {
            HttpHeaders headers = sessionManager.invalidateSession(0L);
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
	 * Refresh CookieOrToken.
	 *
	 * @return the refresh CookieOrToken status.
	 * @throws Exception
	 */
    @ApiVersion("1")
    @RequestMapping(path = "/$Refresh", method = RequestMethod.POST)
    public ResponseEntity refreshCookieOrToken(@CookieValue(value = SESSION_COOKIE, required = false) String sessionCookie,
                                               @RequestHeader(value="X-Refresh-Token", required=false) String refreshTokenHeader) throws Exception {
        String refreshToken = authorizationValidator.validateAndGetRefreshToken(sessionCookie, refreshTokenHeader);
        TokenResponse tokenResponse = iamUtil.exchangeForToken(REFRESH_TOKEN, refreshToken, null);
        if(sessionCookie != null) {
            HttpHeaders headers = sessionManager.createSession(tokenResponse.getAccess_token(), refreshToken,
                    tokenResponse.getExpires_in());
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
	 * Exchange CookieOrToken.
	 *
	 * @return the exchange CookieOrToken status.
	 * @throws Exception
	 */
    @ApiVersion("1")
    @RequestMapping(path = "/$Exchange", method = RequestMethod.POST)
    public ResponseEntity<Session> exchangeCookieWithToken(@RequestHeader(name = "X-OneSpace-Cookie") String sessionCookie, @Valid @RequestBody ClientCredentials clientCredentials)
            throws InvalidClientException, InvalidTokenException {
        if (!iamUtil.isValidClient(clientCredentials.getClientId(), clientCredentials.getClientSecret())) {
            throw new InvalidClientException(INVALID_CLIENT_ERR_CODE);
        }
        Session session = sessionManager.getSession(sessionCookie);
        session.setExpiresIn(iamUtil.getTokenExpirationTime(session.getAccessToken()));
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    private static String getParamValue(MultiValueMap<String, String> paramMap, String paramName) {
        return paramMap.containsKey(paramName) ? paramMap.get(paramName).get(0) : null;
    }

    private TokenResponse validateAndExchangeForToken(MultiValueMap<String, String> paramMap) throws BadRequestException {
        TokenResponse tokenResponse;
        String code = getParamValue(paramMap, HSP_IAM_CODE);
        String assertion = getParamValue(paramMap, HSP_IAM_ASSERTION);
        String redirectUri = getParamValue(paramMap, HSP_IAM_REDIRECT_URI);
        if (redirectUri == null) {
            throw new BadRequestException(ErrorMessages.HSP_IAM_REDIRECT_URI_PARAMETER_MISSING);
        }
        if (StringUtils.isBlank(code) && StringUtils.isBlank(assertion)) {
            throw new BadRequestException(ErrorMessages.HSP_IAM_CODE_ASSERTION_PARAMETER_MISSING);
        }
        if (code != null) {
            tokenResponse = iamUtil.exchangeForToken(HSP_IAM_CODE, code, redirectUri);
        } else {
            tokenResponse = iamUtil.exchangeForToken(HSP_IAM_ASSERTION, assertion, redirectUri);
        }
        return tokenResponse;
    }
}
