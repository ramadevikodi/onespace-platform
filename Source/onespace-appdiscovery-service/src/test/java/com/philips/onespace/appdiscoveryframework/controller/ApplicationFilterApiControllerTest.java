package com.philips.onespace.appdiscoveryframework.controller;
import static com.philips.onespace.util.IamConstants.BEARER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.philips.onespace.appdiscoveryframework.service.FilterCriteriaService;
import com.philips.onespace.appdiscoveryframework.util.validation.AuthorizationValidator;
import com.philips.onespace.dto.ApplicationFilter;
import com.philips.onespace.exception.InvalidTokenException;
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


class ApplicationFilterApiControllerTest{

    @Mock
    private FilterCriteriaService filterCriteriaService;

    @Mock
    private AuthorizationValidator authorizationValidator;

    @InjectMocks
    private ApplicationFilterApiController applicationFilterApiController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFilterCriteriaSuccess() throws InvalidTokenException, BadRequestException {

        String sessionCookie = "validSessionCookie";
        String authorization = BEARER + " validToken";
        String token = "validToken";
        List<ApplicationFilter> mockCriteria = new ArrayList<>();
        when(authorizationValidator.validateAndGetToken(sessionCookie, authorization)).thenReturn(token);
        when(filterCriteriaService.getCriteriaDataForCurrentUser(token)).thenReturn(mockCriteria);
        ResponseEntity<List<ApplicationFilter>> response = applicationFilterApiController.getFilterCriteria(sessionCookie, authorization);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCriteria, response.getBody());
        verify(authorizationValidator, times(1)).validateAndGetToken(sessionCookie, authorization);
        verify(filterCriteriaService, times(1)).getCriteriaDataForCurrentUser(token);
    }
    @Test
    void testGetFilterCriteriaBadRequest() throws InvalidTokenException, BadRequestException {
                String sessionCookie = null;
        String authorization = null;
        when(authorizationValidator.validateAndGetToken(sessionCookie, authorization)).thenThrow(new BadRequestException("Bad request"));
        assertThrows(BadRequestException.class, () -> {
            applicationFilterApiController.getFilterCriteria(sessionCookie, authorization);
        });

        verify(authorizationValidator, times(1)).validateAndGetToken(sessionCookie, authorization);
        verify(filterCriteriaService, never()).getCriteriaDataForCurrentUser(anyString());
    }
}

