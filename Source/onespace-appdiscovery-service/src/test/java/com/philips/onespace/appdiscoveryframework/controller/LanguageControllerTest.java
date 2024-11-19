package com.philips.onespace.appdiscoveryframework.controller;

import com.philips.onespace.appdiscoveryframework.service.LanguageServiceImpl;
import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.dto.Value;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

class LanguageControllerTests {

    @Mock
    private RateLimitService rateLimitService;

    @Mock
    private LanguageServiceImpl languageService;

    @InjectMocks
    private LanguageController languageController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListLanguagesSuccess() throws BadRequestException {

        List<Value> mockLanguages = new ArrayList<>();

        when(languageService.getLanguages()).thenReturn(mockLanguages);
        when(rateLimitService.consume()).thenReturn(Boolean.TRUE);
        ResponseEntity<List<Value>> response = languageController.listLanguages();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockLanguages, response.getBody());
        verify(languageService, times(1)).getLanguages();
        verify(rateLimitService, times(1)).consume();
    }

    @Test
    void testListLanguagesRateLimitExceeded() throws BadRequestException {

        when(rateLimitService.consume()).thenReturn(Boolean.FALSE);
        ResponseEntity<List<Value>> response = languageController.listLanguages();
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        verify(rateLimitService, times(1)).consume();

    }
}

