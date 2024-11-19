/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: PropositionApiController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.philips.onespace.appdiscoveryframework.service.interfaces.PropositionService;
import com.philips.onespace.dto.Proposition;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.validator.ApiVersion;

import jakarta.annotation.security.RolesAllowed;

@RestController
@Validated
@RequestMapping("/Proposition")
public class PropositionApiController {

	@Autowired
	private PropositionService propositionService;

	/**
	 * Get propositions.
	 *
	 * @return the list of proposition objects.
	 */
	@ApiVersion("1")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@RolesAllowed({ "EPO_APP.LIST" })
	public ResponseEntity<List<Proposition>> listPropositions()
			throws ResourceNotFoundException, InvalidTokenException, BadRequestException {
		return ResponseEntity.ok(propositionService.listPropositions());
	}
}