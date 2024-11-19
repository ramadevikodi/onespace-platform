package com.philips.onespace.appdiscoveryframework.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.philips.onespace.appdiscoveryframework.service.DeploymentModeService;
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

import java.util.ArrayList;
import java.util.List;

class DeploymentModeControllerTest {

    @Mock
    private DeploymentModeService deploymentModeService;

    @Mock
    private RateLimitService rateLimitService;

    @InjectMocks
    private DeploymentModeController deploymentModeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listDeploymentModes_whenRateLimitAllows_shouldReturnOk() throws BadRequestException {

        List<Value> deploymentModes = new ArrayList<>();
        deploymentModes.add(new Value());
        when(deploymentModeService.getDeploymentMode()).thenReturn(deploymentModes);
        when(rateLimitService.consume()).thenReturn(Boolean.TRUE);
        ResponseEntity<List<Value>> response = deploymentModeController.listDeploymentModes();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deploymentModes, response.getBody());
        verify(rateLimitService, times(1)).consume();
        verify(deploymentModeService, times(1)).getDeploymentMode();
    }

    @Test
    void listDeploymentModes_whenRateLimitExceeded_shouldReturnTooManyRequests() throws BadRequestException {

        when(rateLimitService.consume()).thenReturn(Boolean.FALSE);
        ResponseEntity<List<Value>> response = deploymentModeController.listDeploymentModes();
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertNull(response.getBody());
        verify(rateLimitService, times(1)).consume();
       // verify(deploymentModeService, never()).getDeploymentMode();
    }
}
