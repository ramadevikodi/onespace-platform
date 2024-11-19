
package com.philips.onespace.appdiscoveryframework.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import com.philips.onespace.appdiscoveryframework.service.interfaces.AppListingService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.philips.onespace.appdiscoveryframework.mapper.ApplicationMapper;
import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.appdiscoveryframework.util.validation.AuthorizationValidator;
import com.philips.onespace.dto.Application;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;


@ExtendWith(MockitoExtension.class)
class ApplicationListingApiControllerTest {

    @InjectMocks
    private ApplicationListingApiController applicationListingApiController;
    @Mock
    private RateLimitService rateLimitService;

    @Mock
    private ApplicationMapper applicationMapper;

    @Mock
    private AuthorizationValidator authorizationValidator;
    @Mock
    private AppListingService appListingService;
    private String token = "test-token";

    @Test
    void testGetAllApplications_ReturnsEmptyList() throws InvalidTokenException, ResourceNotFoundException, BadRequestException {
        Page<ApplicationEntity> applicationPage = mock(Page.class);
        when(appListingService.listApplications(any(), any(),any(), any())).thenReturn(applicationPage);
        when(rateLimitService.consume()).thenReturn(Boolean.TRUE);
        when(applicationPage.isEmpty()).thenReturn(Boolean.TRUE);
        ResponseEntity<List<Application>> applications = applicationListingApiController
                .listApplications("", "", null,
                        "lastModifiedDateTime","DESC", null);
        Assertions.assertNotNull(applications.getBody());
        assertEquals(HttpStatus.OK, applications.getStatusCode());
    }

    @Test
    void testGetAllApplications() throws InvalidTokenException, ResourceNotFoundException, BadRequestException {
        Pageable pageable = PageRequest.of(0, 20);
        Page<ApplicationEntity> applicationPage = mock(Page.class);
        when(appListingService.listApplications(any(), any(),any(), any())).thenReturn(applicationPage);
        when(rateLimitService.consume()).thenReturn(Boolean.TRUE);
        when(applicationPage.isEmpty()).thenReturn(Boolean.FALSE);
        when(applicationMapper.entityToDto(any(Page.class))).thenReturn(Collections.emptyList());
        ResponseEntity<List<Application>> response = applicationListingApiController
                .listApplications("", "", null, "lastModifiedDateTime", "DESC", pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
    @Test
    void testGetAllApplications_TooManyRequests() throws InvalidTokenException, ResourceNotFoundException, BadRequestException {
        Pageable pageable = PageRequest.of(0, 20);
        when(rateLimitService.consume()).thenReturn(Boolean.FALSE);

        ResponseEntity<List<Application>> response = applicationListingApiController
                .listApplications("", "", null, "lastModifiedDateTime", "DESC", pageable);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
    }

    @Test
    void testGetAllApplications_RateLimitExceeded() throws InvalidTokenException, ResourceNotFoundException, BadRequestException {
        Pageable pageable = PageRequest.of(0, 20);
        when(rateLimitService.consume()).thenReturn(Boolean.FALSE);

        ResponseEntity<List<Application>> applications = applicationListingApiController
                .listApplications("", "", null,  "lastModifiedDateTime", "DESC", pageable);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, applications.getStatusCode());
    }
    @Test
    void testFilterApplications_Success() throws Exception {
        UUID statusId = UUID.randomUUID();
        Map<String, String> criteria = new HashMap<>();
        Pageable pageable = PageRequest.of(0,10);
        Page<ApplicationEntity> applicationPage = mock(Page.class);

        when(authorizationValidator.validateAndGetToken(anyString(), anyString())).thenReturn(token);
        when(appListingService.listApplications(any(), any(), any(), any()))
                .thenReturn(applicationPage);
        when(applicationPage.isEmpty()).thenReturn(Boolean.FALSE);
        when(applicationMapper.entityToDto(any(Page.class))).thenReturn(Collections.emptyList());
        ResponseEntity<List<Application>> response = applicationListingApiController.filterApplications(
                "cookie", "auth", statusId, criteria, "name", "ASC", pageable);
        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }
    @Test
    void testFilterApplications_EmptyResult() throws Exception {

        UUID statusId = UUID.randomUUID();
        Map<String, String> criteria = new HashMap<>();
        Pageable pageable = PageRequest.of(0,10);
        Page<ApplicationEntity> applicationPage = mock(Page.class);

        when(authorizationValidator.validateAndGetToken(anyString(), anyString())).thenReturn(token);
        when(appListingService.listApplications(any(), any(), any(), any()))
                .thenReturn(applicationPage);
        when(applicationPage.isEmpty()).thenReturn(Boolean.TRUE);

        ResponseEntity<List<Application>> response = applicationListingApiController.filterApplications(
                "cookie", "auth", statusId, criteria, "name", "ASC", pageable);

        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

}

