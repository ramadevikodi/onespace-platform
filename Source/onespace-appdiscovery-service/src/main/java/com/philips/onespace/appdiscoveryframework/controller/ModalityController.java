/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ModalityController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.philips.onespace.appdiscoveryframework.service.ModalityService;
import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.dto.Value;
import com.philips.onespace.validator.ApiVersion;

import jakarta.annotation.security.RolesAllowed;

@Validated
@RequestMapping("/Modality")
@RestController
public class ModalityController {
	
	@Autowired
	private RateLimitService rateLimitService;

	@Autowired
	private ModalityService modalityService;

	/**
	 * Get modalities.
	 *
	 * @return the list of modality objects.
	 */
	@ApiVersion("1")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@RolesAllowed({ "EPO_APP.LIST" })
	public ResponseEntity<List<Value>> listModalities() throws BadRequestException {
		List<Value> modalities = modalityService.getModalities();
		if (rateLimitService.consume()) {
			return new ResponseEntity<>(modalities, HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		}
	}

}
