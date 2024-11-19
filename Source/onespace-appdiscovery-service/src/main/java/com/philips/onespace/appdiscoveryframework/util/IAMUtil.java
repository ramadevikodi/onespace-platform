/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: IAMUtil.java
 */

package com.philips.onespace.appdiscoveryframework.util;

import com.philips.onespace.exception.InvalidClientException;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.logging.LogExecutionTime;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.model.Organization;
import com.philips.onespace.model.TokenResponse;
import com.philips.onespace.util.Constants;
import com.philips.onespace.util.LocaleUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.UUID;

import static com.philips.onespace.logging.LoggingAspect.logData;
import static com.philips.onespace.logging.LoggingAspect.maskData;
import static com.philips.onespace.util.IamConstants.*;

@Component
public class IAMUtil {
    private final String tokenBaseUrl;
    private final String baseUrl;
    private final String tokenUrl;
    private final String clientId;
    private final String clientCredentials;
    private RestTemplate restTemplate;
    private int timeRemainingForTokenExpiry;

    private final String introspectUrl;
    private final String revokeUrl;

    @Autowired
    private LocaleUtil localeUtil;

    public IAMUtil(@Value("${iam.tokenbaseurl}") String tokenBaseUrl, @Value("${iam.baseurl}") String baseUrl, @Value("${iam.tokenurl}") String tokenUrl, @Value("${iam.clientId}") String clientId, @Value("${iam.clientSecret}") String clientCredentials
            , @Value("${iam.timeRemainingForTokenExpiry}") int timeRemainingForTokenExpiry, @Value("${iam.introspectUrl}") String introspectUrl, @Value("${iam.revokeUrl}") String revokeUrl) {
        this.tokenBaseUrl = tokenBaseUrl;
        this.baseUrl = baseUrl;
        this.tokenUrl = tokenUrl;
        this.clientId = clientId;
        this.clientCredentials = clientCredentials;
        this.timeRemainingForTokenExpiry = timeRemainingForTokenExpiry;
        this.introspectUrl = introspectUrl;
        this.revokeUrl = revokeUrl;
        this.restTemplate = new RestTemplate();
    }

    @LogExecutionTime
    public IntrospectionResponse introspectToken(String token) throws InvalidTokenException {

        final String encodedBasicAuthorization = "Basic " + HttpHeaders.encodeBasicAuth(clientId, clientCredentials,
                StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-version", "3");
        headers.set("Authorization", encodedBasicAuthorization);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        logData(" Invoking ExtIAM introspect API with token=" + maskData(token));
        ResponseEntity<IntrospectionResponse> result = this.restTemplate.exchange(baseUrl + introspectUrl, HttpMethod.POST, request, IntrospectionResponse.class);
        IntrospectionResponse introspectResponse = result.getBody();
        if (introspectResponse != null && !introspectResponse.getActive()) {
            throw new InvalidTokenException("The authentication token is inactive or expired");
        }
        if (introspectResponse != null) {
            String userPreferredLanguage = localeUtil.getUserPreferredLanguage(token, introspectResponse.getSub());
            introspectResponse.setUserPreferredLanguage(userPreferredLanguage);
            Locale locale = new Locale(userPreferredLanguage);
            localeUtil.setLocale(locale);
            logData(" Introspection response : "+ introspectResponse.maskedResponse());
        }
        return introspectResponse;
    }

    @LogExecutionTime
    public void revokeToken(String token) {
        final String encodedBasicAuthorization = "Basic " + HttpHeaders.encodeBasicAuth(clientId, clientCredentials,
                StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-version", "2");
        headers.set("Authorization", encodedBasicAuthorization);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token",token);
        logData(" Invoking ExtIAM revoke API with token ="+maskData(token));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        this.restTemplate.exchange(baseUrl + revokeUrl, HttpMethod.POST, request, Object.class);
    }

    @LogExecutionTime
    public TokenResponse exchangeForToken(String grantType, String grantTypeValue, String redirectUri) throws HttpClientErrorException {
        HttpHeaders headers = createClientCredentialsAuthHeader(clientId, clientCredentials);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> maskedMap = new LinkedMultiValueMap<>();
        if(grantType.equals(HSP_IAM_CODE)) {
            map.add(HSP_IAM_GRANT_TYPE, HSP_IAM_AUTH_CODE_GRANT_TYPE);
            maskedMap.add(HSP_IAM_GRANT_TYPE, HSP_IAM_AUTH_CODE_GRANT_TYPE);
            map.add(HSP_IAM_CODE, grantTypeValue);
            maskedMap.add(HSP_IAM_CODE, maskData(grantTypeValue));
            map.add(HSP_IAM_REDIRECT_URI, redirectUri);
            maskedMap.add(HSP_IAM_REDIRECT_URI, redirectUri);
        } else if (grantType.equals(HSP_IAM_ASSERTION)) {
            map.add(HSP_IAM_GRANT_TYPE, HSP_IAM_SAML_GRANT_TYPE);
            maskedMap.add(HSP_IAM_GRANT_TYPE, HSP_IAM_SAML_GRANT_TYPE);
            map.add(HSP_IAM_ASSERTION, grantTypeValue);
            maskedMap.add(HSP_IAM_ASSERTION, maskData(grantTypeValue));
            map.add(HSP_IAM_REDIRECT_URI, redirectUri);
            maskedMap.add(HSP_IAM_REDIRECT_URI, redirectUri);
        } else if (grantType.equals(Constants.REFRESH_TOKEN)) {
            map.add(HSP_IAM_GRANT_TYPE, Constants.REFRESH_TOKEN);
            maskedMap.add(HSP_IAM_GRANT_TYPE, Constants.REFRESH_TOKEN);
            map.add(Constants.REFRESH_TOKEN, grantTypeValue);
            maskedMap.add(Constants.REFRESH_TOKEN, maskData(grantTypeValue));
        }
        logData(" Invoking token exchange request, "+ maskedMap);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<TokenResponse> result = this.restTemplate.exchange(tokenBaseUrl + tokenUrl, HttpMethod.POST, request, TokenResponse.class);
        TokenResponse tokenResponse = result.getBody();
        if(tokenResponse != null){
            logData(" Token exchange response ="+ tokenResponse.toString());
        }
        return tokenResponse;
    }

    @LogExecutionTime
    public boolean isValidClient(String clientId, String clientSecret) throws HttpClientErrorException, InvalidClientException {
        HttpHeaders headers = createClientCredentialsAuthHeader(clientId, clientSecret);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(HSP_IAM_GRANT_TYPE, Constants.CLIENT_CREDENTIALS);
        logData("Invoking ExtIAM token exchange - client credentials request : " + map);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<TokenResponse> result = null;
        try {
            result = this.restTemplate.exchange(tokenBaseUrl + tokenUrl, HttpMethod.POST, request, TokenResponse.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return false;
            }
            throw ex;
        }
        return result.getStatusCode().is2xxSuccessful();
    }

    public boolean checkIfTokenContainsRole(IntrospectionResponse introspectionResponse, String roleName) {
        return introspectionResponse.getOrganizations().getOrganizationList().stream()
                .filter(o -> o.getRoles().contains(roleName)).findFirst().isPresent();
    }

    public String findUserOrg(IntrospectionResponse introspectionResponse, String roleName) {
        Organization organization = introspectionResponse.getOrganizations().getOrganizationList().stream()
                .filter(o -> o.getRoles().contains(roleName)).findFirst().get();
        return organization.getOrganizationId();
    }

    public UUID findUserOrg(String token, String roleName) throws InvalidTokenException {
        IntrospectionResponse introspectionResponse = introspectToken(token);
        return UUID.fromString(findUserOrg(introspectionResponse, roleName));
    }

    public boolean hasRole(String token, String roleName) throws InvalidTokenException {
        IntrospectionResponse introspectionResponse = introspectToken(token);
        return checkIfTokenContainsRole(introspectionResponse, roleName);
    }

    public boolean checkIfTokenIsExpiring(Long tokenExpirationVal) {
        if(getTokenExpirationTimeInSecs(tokenExpirationVal) < timeRemainingForTokenExpiry) {
            return true;
        }
        return false;
    }

    public int getTokenExpirationTime(String accessToken) throws InvalidTokenException {
        IntrospectionResponse introspectionResponse = introspectToken(accessToken);
        return getTokenExpirationTimeInSecs(introspectionResponse.getExp());
    }

    private int getTokenExpirationTimeInSecs(Long tokenExpirationVal) {
        return (int) ChronoUnit.SECONDS.between(Instant.now(), Instant.ofEpochSecond(tokenExpirationVal));
    }

    private static HttpHeaders createClientCredentialsAuthHeader(String clientId, String clientSecret) {
        final String encodedBasicAuthorization = "Basic " + HttpHeaders.encodeBasicAuth(clientId, clientSecret,
                StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", encodedBasicAuthorization);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }
}
