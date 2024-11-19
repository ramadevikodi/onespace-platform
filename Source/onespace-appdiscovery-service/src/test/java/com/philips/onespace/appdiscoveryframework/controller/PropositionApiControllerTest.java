package com.philips.onespace.appdiscoveryframework.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import com.philips.onespace.appdiscoveryframework.service.interfaces.PropositionService;
import com.philips.onespace.dto.Proposition;

public class PropositionApiControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(PropositionApiControllerTest.class);
    @InjectMocks
    PropositionApiController propositionApiController;

    @Mock
    PropositionService propositionService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @WithMockUser(roles = "EPO_APP.LIST")
    @Test
    public void testListPropositions() throws Exception {
        Proposition proposition1 = new Proposition();
        Proposition proposition2 = new Proposition();
        List<Proposition> propositions = Arrays.asList(proposition1, proposition2);

        when(propositionService.listPropositions()).thenReturn(propositions);

        ResponseEntity<List<Proposition>> response = null; // Initialize response with null

        try {
            response = propositionApiController.listPropositions();
        } catch (Exception e) {
            logger.error("Resource not found or invalid token {}", e.toString());
        }
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(propositions, response.getBody());
    }
}

