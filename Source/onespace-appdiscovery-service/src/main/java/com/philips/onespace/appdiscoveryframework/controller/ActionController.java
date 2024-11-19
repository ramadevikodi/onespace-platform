/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ActionController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import static com.philips.onespace.util.ErrorMessages.NO_RECORDS_FOUND;

import java.util.Collections;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.philips.onespace.appdiscoveryframework.service.interfaces.ActionService;
import com.philips.onespace.dto.Action;
import com.philips.onespace.dto.ActionId;
import com.philips.onespace.validator.ApiVersion;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/Action")
public class ActionController {

    @Autowired
    private ActionService actionService;

    /**
	 * Save action.
	 *
	 * @param action the action DTO
	 * @return the posted action DTO with unique ID.
	 * @throws BadRequestException 
	 */
    @ApiVersion("1")
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @RolesAllowed({"EPO_APP.LIST"})
    public ResponseEntity<ActionId> createAction(@RequestBody @Valid Action action) throws BadRequestException {
    	ActionId actionResponse = actionService.createAction(action);
        return new ResponseEntity<>(actionResponse, HttpStatus.CREATED);
    }

    /**
	 * Get actions.
	 *
	 * @return the action list.
	 * @throws BadRequestException 
	 */
    @ApiVersion("1")
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @RolesAllowed({"EPO_APP.LIST"})
    public ResponseEntity<List<Action>> getActions() throws BadRequestException {
    	List<Action> actions = actionService.getActions();
        if (actions.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(actions, HttpStatus.OK);
    }

    /**
	 * Update action.
	 *
	 * @param action the action DTO
	 * @return the posted action with unique ID.
	 * @throws BadRequestException 
	 */
    @ApiVersion("1")
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PATCH)
    @RolesAllowed({"EPO_APP.LIST"})
    public ResponseEntity<String> patchAction(@RequestBody @Valid List<Action> action) throws BadRequestException {
        int updatedRowCount = actionService.updateStatus(action);
        if (updatedRowCount > 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(NO_RECORDS_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
