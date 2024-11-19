/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: BusinessUnitController.java
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

import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.BusinessUnitService;
import com.philips.onespace.dto.BusinessUnit;
import com.philips.onespace.validator.ApiVersion;

import jakarta.annotation.security.RolesAllowed;

@Validated
@RequestMapping("/Business")
@RestController
public class BusinessUnitController {

	@Autowired
	private BusinessUnitService businessUnitService;

	@Autowired
	private RateLimitService rateLimitService;

	/**
	 * Get business categories status.
	 *
	 * @return the list of business categories objects.
	 */
	@ApiVersion("1")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@RolesAllowed({ "EPO_APP.LIST" })
	public ResponseEntity<List<BusinessUnit>> listBusinessCategories() throws BadRequestException {
		List<BusinessUnit> businessCategories = businessUnitService.getBusinessCategories();
		if (rateLimitService.consume()) {
			return new ResponseEntity<>(businessCategories, HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		}
	}
}
