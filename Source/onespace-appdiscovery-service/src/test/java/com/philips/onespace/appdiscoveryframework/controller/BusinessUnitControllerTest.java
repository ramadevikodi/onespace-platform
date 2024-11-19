package com.philips.onespace.appdiscoveryframework.controller;

import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.BusinessUnitService;
import com.philips.onespace.dto.BusinessUnit;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessUnitControllerTest {

    @Mock
    private BusinessUnitService businessUnitService;

    @Mock
    private RateLimitService rateLimitService;

    @InjectMocks
    private BusinessUnitController businessUnitController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testListBusinessCategories_WithRateLimitNotExceed() throws BadRequestException {

        List<BusinessUnit> businessUnits = new ArrayList<>();

        when(businessUnitService.getBusinessCategories()).thenReturn(businessUnits);
        when(rateLimitService.consume()).thenReturn(Boolean.TRUE);

        ResponseEntity<List<BusinessUnit>> response = businessUnitController.listBusinessCategories();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(businessUnits);


        verify(businessUnitService, times(1)).getBusinessCategories();
        verify(rateLimitService, times(1)).consume();
    }

    @Test
    void testListBusinessCategories_WithTooManyRequests() throws BadRequestException {

        when(rateLimitService.consume()).thenReturn(Boolean.FALSE);

        ResponseEntity<List<BusinessUnit>> response = businessUnitController.listBusinessCategories();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        assertThat(response.getBody()).isNull();

        verify(rateLimitService, times(1)).consume();

    }

    @Test
    void testListBusinessCategories_WhenBusinessServiceThrowsException() {

        when(businessUnitService.getBusinessCategories()).thenThrow(new RuntimeException("Service Error"));
        when(rateLimitService.consume()).thenReturn(Boolean.TRUE);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            businessUnitController.listBusinessCategories();
        });

        assertThat(exception.getMessage()).isEqualTo("Service Error");

        verify(rateLimitService, times(0)).consume();
    }


}
