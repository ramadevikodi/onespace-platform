package com.philips.onespace.appdiscoveryframework.controller;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.philips.onespace.appdiscoveryframework.service.ModalityService;
import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.dto.Value;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;

class ModalityControllerTest {

    @Mock
    private RateLimitService rateLimitService;

    @Mock
    private ModalityService modalityService;

    @InjectMocks
    private ModalityController modalityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListModalities_WhenRateLimitConsumed_ShouldReturnModalitiesWithOK() throws BadRequestException {

        List<Value> mockModalities = Arrays.asList(new Value(), new Value());
        when(modalityService.getModalities()).thenReturn(mockModalities);
        when(rateLimitService.consume()).thenReturn(Boolean.TRUE);
        ResponseEntity<List<Value>> response = modalityController.listModalities();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockModalities, response.getBody());
        verify(modalityService, times(1)).getModalities();
        verify(rateLimitService, times(1)).consume();
    }


}