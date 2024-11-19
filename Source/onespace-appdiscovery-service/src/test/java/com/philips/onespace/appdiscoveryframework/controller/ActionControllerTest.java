/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ActionControllerTest.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.philips.onespace.appdiscoveryframework.service.interfaces.ActionService;
import com.philips.onespace.dto.Action;
import com.philips.onespace.dto.ActionId;

class ActionControllerTest {

    @Mock
    private ActionService actionService;

    @InjectMocks
    private ActionController actionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testCreateAction() throws Exception {
        Action action = new Action();
        ActionId actionId = new ActionId();
        when(actionService.createAction(action)).thenReturn(actionId);

        ResponseEntity<ActionId> response = actionController.createAction(action);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(actionId, response.getBody());
        verify(actionService, times(1)).createAction(action);
    }

    @Test
    void testGetActions() throws Exception {
        List<Action> actions = Arrays.asList(new Action(), new Action());
        when(actionService.getActions()).thenReturn(actions);

        ResponseEntity<List<Action>> response = actionController.getActions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(actions, response.getBody());
        verify(actionService, times(1)).getActions();
    }

    @Test
    void testGetActions_EmptyList() throws Exception {
        when(actionService.getActions()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Action>> response = actionController.getActions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
        verify(actionService, times(1)).getActions();
    }

    @Test
    void testPatchAction_Success() throws Exception {
        List<Action> actions = Arrays.asList(new Action(), new Action());
        when(actionService.updateStatus(actions)).thenReturn(2);

        ResponseEntity<String> response = actionController.patchAction(actions);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(actionService, times(1)).updateStatus(actions);
    }

    @Test
    void testPatchAction_NoRecordsFound() throws Exception {
        List<Action> actions = Arrays.asList(new Action(), new Action());
        when(actionService.updateStatus(actions)).thenReturn(0);

        ResponseEntity<String> response = actionController.patchAction(actions);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(actionService, times(1)).updateStatus(actions);
    }
}
