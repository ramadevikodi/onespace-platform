package com.philips.onespace.appdiscoveryframework.controller;


import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.appdiscoveryframework.service.SpecialityServiceImpl;

import com.philips.onespace.dto.Value;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpecialityControllerTest {

    @InjectMocks
    private SpecialityController specialityController;

    @Mock
    private SpecialityServiceImpl specialityService;

    @Mock
    private RateLimitService rateLimitService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListSpeciality_WithRateLimitSuccess() throws Exception {

        List<Value> mockSpecialities = Arrays.asList(new Value(UUID.randomUUID(), "Speciality 1", ""), new Value(UUID.randomUUID(), "Speciality 2", ""));

        when(specialityService.getSpecialities()).thenReturn(mockSpecialities);
        when(rateLimitService.consume()).thenReturn(Boolean.TRUE);

        ResponseEntity<List<Value>> response = specialityController.listSpeciality();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Speciality 1", response.getBody().get(0).getName());
        assertEquals("Speciality 2", response.getBody().get(1).getName());


        verify(specialityService, times(1)).getSpecialities();
        verify(rateLimitService, times(1)).consume();

    }

    @Test
    void testListSpeciality_WithRateLimitExceeded() throws Exception {

        when(rateLimitService.consume()).thenReturn(Boolean.FALSE);

        ResponseEntity<List<Value>> response = specialityController.listSpeciality();

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertNull(response.getBody());

        verify(rateLimitService, times(1)).consume();
    }


}
