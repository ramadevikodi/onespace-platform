package com.philips.onespace.appdiscoveryframework.controller;

import com.philips.onespace.appdiscoveryframework.service.ApplicationStatusService;
import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.dto.Value;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ApplicationStatusControllerTest {

    @Mock
    private RateLimitService rateLimitService;

    @Mock
    private ApplicationStatusService applicationStatusService;

    @InjectMocks
    private ApplicationStatusController applicationStatusController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListApplicationStatus_WithRateLimitNotExceeded() throws BadRequestException {
        List<Value> applicationStatuses = Arrays.asList(new Value(UUID.randomUUID(), "Status 1",""), new Value(UUID.randomUUID(), "Status 2", ""));

        when(applicationStatusService.getApplicationStatus()).thenReturn(applicationStatuses);
        when(rateLimitService.consume()).thenReturn(Boolean.TRUE);

        ResponseEntity<List<Value>> response = applicationStatusController.listApplicationStatus();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Status 1", response.getBody().get(0).getName());
        assertEquals("Status 2", response.getBody().get(1).getName());


        verify(applicationStatusService, times(1)).getApplicationStatus();
        verify(rateLimitService, times(1)).consume();

    }


    @Test
    void testListApplicationStatus_TooManyRequests() throws BadRequestException {

        when(rateLimitService.consume()).thenReturn(Boolean.FALSE);

        ResponseEntity<List<Value>> response = applicationStatusController.listApplicationStatus();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        assertThat(response.getBody()).isNull();

        verify(rateLimitService, times(1)).consume();


    }


}
