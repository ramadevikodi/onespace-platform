package com.philips.onespace.appdiscoveryframework.controller;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.philips.onespace.appdiscoveryframework.service.interfaces.LicensingService;
import com.philips.onespace.appdiscoveryframework.util.validation.AuthorizationValidator;
import com.philips.onespace.dto.LicenseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

class LicensingControllerTest {

    @Mock
    private LicensingService licensingService;

    @Mock
    private AuthorizationValidator authorizationValidator;

    @InjectMocks
    private LicensingController licensingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumeLicense() throws Exception {

        LicenseRequest licenseRequest = new LicenseRequest();
        licenseRequest.setApplicationId(UUID.randomUUID());
        String sessionCookie = "sessionCookie";
        String authorization = "authorization";
        String token = "token";

        when(authorizationValidator.validateAndGetToken(sessionCookie, authorization)).thenReturn(token);
        doNothing().when(licensingService).consumeLicense(token, licenseRequest.getApplicationId());
        ResponseEntity response = licensingController.consumeLicense(licenseRequest, sessionCookie, authorization);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(authorizationValidator, times(1)).validateAndGetToken(sessionCookie, authorization);
        verify(licensingService, times(1)).consumeLicense(token, licenseRequest.getApplicationId());
    }

    @Test
    void testReleaseLicense() throws Exception {

        LicenseRequest licenseRequest = new LicenseRequest();
        licenseRequest.setApplicationId(UUID.randomUUID());
        String sessionCookie = "sessionCookie";
        String authorization = "authorization";
        String token = "token";

        when(authorizationValidator.validateAndGetToken(sessionCookie, authorization)).thenReturn(token);
        doNothing().when(licensingService).releaseLicense(token, licenseRequest.getApplicationId());
        ResponseEntity response = licensingController.releaseLicense(licenseRequest, sessionCookie, authorization);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(authorizationValidator, times(1)).validateAndGetToken(sessionCookie, authorization);
        verify(licensingService, times(1)).releaseLicense(token, licenseRequest.getApplicationId());
    }
}
