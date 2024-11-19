/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: SpecialityController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.appdiscoveryframework.service.SpecialityServiceImpl;
import com.philips.onespace.dto.Value;
import com.philips.onespace.validator.ApiVersion;

@RestController
@Validated
@RequestMapping("/Speciality")
public class SpecialityController {

	@Autowired
	private SpecialityServiceImpl specialityService;

	@Autowired
	private RateLimitService rateLimitService;

	/**
	 * Get specialities.
	 *
	 * @return the list of speciality objects.
	 */
	@ApiVersion("1")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<List<Value>> listSpeciality() throws Exception {
		List<Value> specialities = specialityService.getSpecialities();
		if (rateLimitService.consume()) {
			return new ResponseEntity<>(specialities, HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		}
	}
}
