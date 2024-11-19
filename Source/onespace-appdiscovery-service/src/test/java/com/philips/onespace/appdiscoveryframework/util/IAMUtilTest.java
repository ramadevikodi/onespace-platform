package com.philips.onespace.appdiscoveryframework.util;

import com.philips.onespace.exception.InvalidClientException;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.model.Organization;
import com.philips.onespace.model.Organizations;
import com.philips.onespace.model.TokenResponse;
import com.philips.onespace.util.LocaleUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class IAMUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private LocaleUtil localeUtil;

    String tokenBaseUrl = "http://example.com/token";
    String baseUrl = "http://example.com";
    String tokenUrl = "/oauth/token";
    String clientId = "clientId";
    String clientCredentials = "dummyCredentials";
    int timeRemainingForTokenExpiry = 60;

    Boolean truevalue = Boolean.TRUE;
    Boolean falsevalue = Boolean.FALSE;
    String token = "validToken";

    String introspectUrl = "/authorize/oauth2/introspect";
    String revokeUrl =  "/authorize/oauth2/revoke";

    @InjectMocks private IAMUtil iamUtil = new IAMUtil(tokenBaseUrl, baseUrl, tokenUrl, clientId, clientCredentials, timeRemainingForTokenExpiry,introspectUrl,revokeUrl);


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIntrospectTokenValid() throws InvalidTokenException {

        IntrospectionResponse response = new IntrospectionResponse();
        response.setActive(truevalue);
        response.setSub("userSub");
        response.setUsername("32#43$^$%abc@");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(IntrospectionResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        when(localeUtil.getUserPreferredLanguage(anyString(), anyString())).thenReturn("en");

        IntrospectionResponse result = iamUtil.introspectToken(token);

        assertTrue(result.getActive());
        assertEquals("en", result.getUserPreferredLanguage());
    }

    @Test
    void testIntrospectTokenInvalid() {
        IntrospectionResponse response = new IntrospectionResponse();
        response.setActive(falsevalue);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(IntrospectionResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        assertThrows(InvalidTokenException.class, () -> iamUtil.introspectToken(token));
    }

    @Test
    void testRevokeToken() {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class)))
                .thenReturn(mockResponse);

        iamUtil.revokeToken(token);

        verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class));
    }


    static Stream<Arguments> exchangeForTokenProvider() {
        return Stream.of(
                Arguments.of("code", "mockAccessToken"),
                Arguments.of("assertion", "mockAccessToken"),
                Arguments.of("refresh_token", "mockAccessToken")
        );
    }

    @ParameterizedTest
    @MethodSource("exchangeForTokenProvider")
    void testExchangeForToken(String grantType, String expectedAccessToken) {
        String grantTypeValue = "codeValue";
        String redirectUri = "http://example.com/callback";

        TokenResponse response = new TokenResponse();
        response.setAccess_token(expectedAccessToken);
        response.setRefresh_token("refreshToken");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(TokenResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        TokenResponse result = iamUtil.exchangeForToken(grantType, grantTypeValue, redirectUri);

        assertNotNull(result);
        assertNotNull(result.getAccess_token());
        assertEquals(expectedAccessToken, result.getAccess_token());
    }

    @Test
    void testIsValidClient() throws InvalidClientException {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(TokenResponse.class)))
                .thenReturn(new ResponseEntity<>(new TokenResponse(), HttpStatus.OK));

        boolean isValid = iamUtil.isValidClient(clientId, clientCredentials);

        assertTrue(isValid);
    }

    @Test
    void testIsValidClient_Unauthorized() throws InvalidClientException {

        when(restTemplate.exchange(eq(tokenBaseUrl + tokenUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        boolean result = iamUtil.isValidClient(clientId, clientCredentials);
        assertFalse(result, "Expected isValidClient to return false for unauthorized access");
    }

    @Test
    void testIsValidClient_OtherHttpClientErrorException() {

        when(restTemplate.exchange(eq(tokenBaseUrl + tokenUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        assertThrows(HttpClientErrorException.class, () -> {
            iamUtil.isValidClient(clientId, clientCredentials);
        }, "Expected an HttpClientErrorException for non-unauthorized errors");
    }


    @Test
    void testIsValidClientUnauthorized() throws InvalidClientException {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(TokenResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        boolean isValid = iamUtil.isValidClient(clientId, clientCredentials);

        assertFalse(isValid);
    }

    @Test
    void testCheckIfTokenContainsRole() {
        IntrospectionResponse response = new IntrospectionResponse();
        List<Organization> organizationList = new ArrayList<>();
        Organization org = new Organization();
        org.setRoles(Collections.singletonList("admin"));
        organizationList.add(org);
        Organizations organizations = new Organizations();
        organizations.setOrganizationList(organizationList);
        response.setOrganizations(organizations);

        assertTrue(iamUtil.checkIfTokenContainsRole(response, "admin"));
        assertFalse(iamUtil.checkIfTokenContainsRole(response, "user"));
    }

    @Test
    void testFindUserOrg() {
        IntrospectionResponse response = new IntrospectionResponse();
        List<Organization> organizationList = new ArrayList<>();
        Organization org = new Organization();
        org.setRoles(Collections.singletonList("admin"));
        org.setOrganizationId("orgId");
        organizationList.add(org);
        Organizations organizations = new Organizations();
        organizations.setOrganizationList(organizationList);
        response.setOrganizations(organizations);

        String orgId = iamUtil.findUserOrg(response, "admin");

        assertEquals("orgId", orgId);
    }

    @Test
    void testFindUserOrgWithToken() throws InvalidTokenException {
        IntrospectionResponse response = new IntrospectionResponse();
        List<Organization> organizationList = new ArrayList<>();
        Organization org = new Organization();
        org.setRoles(Collections.singletonList("admin"));
        org.setOrganizationId("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        organizationList.add(org);
        Organizations organizations = new Organizations();
        organizations.setOrganizationList(organizationList);
        response.setActive(truevalue);
        response.setSub("userSub");
        response.setUsername("32#43$^$%abc@");
        response.setOrganizations(organizations);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(IntrospectionResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        when(localeUtil.getUserPreferredLanguage(anyString(), anyString())).thenReturn("en");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(IntrospectionResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        UUID orgId = iamUtil.findUserOrg(token, "admin");

        assertEquals(UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c0"), orgId);
    }

    @Test
    void testHasRole() throws InvalidTokenException {
        IntrospectionResponse response = new IntrospectionResponse();
        List<Organization> organizationList = new ArrayList<>();
        Organization org = new Organization();
        org.setRoles(Collections.singletonList("admin"));
        org.setOrganizationId("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        organizationList.add(org);
        Organizations organizations = new Organizations();
        organizations.setOrganizationList(organizationList);
        response.setActive(truevalue);
        response.setSub("userSub");
        response.setUsername("32#43$^$%abc@");
        response.setOrganizations(organizations);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(IntrospectionResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        when(localeUtil.getUserPreferredLanguage(anyString(), anyString())).thenReturn("en");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(IntrospectionResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        assertTrue(iamUtil.hasRole(token, "admin"));
        assertFalse(iamUtil.hasRole(token, "user"));
    }

    @Test
    void testCheckIfTokenIsExpiring() {
        Long tokenExpirationVal = Instant.now().plusSeconds(30).getEpochSecond();
        boolean isExpiring = iamUtil.checkIfTokenIsExpiring(tokenExpirationVal);

        assertTrue(isExpiring);
    }

    @Test
    void testCheckIfTokenIsExpiringFalse() {
        Long tokenExpirationVal = Instant.now().plusSeconds(90).getEpochSecond();
        boolean isExpiring = iamUtil.checkIfTokenIsExpiring(tokenExpirationVal);

        assertFalse(isExpiring);
    }

    @Test
    void testGetTokenExpirationTime() throws InvalidTokenException {
        IntrospectionResponse response = new IntrospectionResponse();
        response.setExp(Instant.now().plusSeconds(30).getEpochSecond());
        List<Organization> organizationList = new ArrayList<>();
        Organization org = new Organization();
        org.setRoles(Collections.singletonList("admin"));
        org.setOrganizationId("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        organizationList.add(org);
        Organizations organizations = new Organizations();
        organizations.setOrganizationList(organizationList);
        response.setActive(truevalue);
        response.setSub("userSub");
        response.setUsername("32#43$^$%abc@");
        response.setOrganizations(organizations);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(IntrospectionResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        when(localeUtil.getUserPreferredLanguage(anyString(), anyString())).thenReturn("en");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(IntrospectionResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        int expirationTime = iamUtil.getTokenExpirationTime(token);

        assertTrue(expirationTime < 30);
    }

    @Test
    void testGetTokenExpirationTimeInvalid() {

        IntrospectionResponse response = new IntrospectionResponse();
        response.setActive(falsevalue);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(IntrospectionResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        assertThrows(InvalidTokenException.class, () -> iamUtil.getTokenExpirationTime(token));
    }
}
