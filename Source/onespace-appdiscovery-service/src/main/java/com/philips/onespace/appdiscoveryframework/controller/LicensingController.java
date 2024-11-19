/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: LicensingController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import static com.philips.onespace.util.Constants.SESSION_COOKIE;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.philips.onespace.appdiscoveryframework.service.interfaces.LicensingService;
import com.philips.onespace.appdiscoveryframework.util.validation.AuthorizationValidator;
import com.philips.onespace.dto.LicenseRequest;
import com.philips.onespace.exception.EntitlementNotFoundException;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.LicenseConsumptionException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.validator.ApiVersion;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/License")
public class LicensingController {
	@Autowired
	private LicensingService licensingService;

	@Autowired
	private AuthorizationValidator authorizationValidator;

	/**
	 * Consume license.
	 *
	 * @return the license consume status.
	 */
	@ApiVersion("1")
	@RolesAllowed({ "EPO_APP.LIST" })
	@RequestMapping(value = "/$Consume", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity consumeLicense(@RequestBody LicenseRequest licenseRequest,
										 @CookieValue(value = SESSION_COOKIE, required = false) String sessionCookie,
										 @RequestHeader(value="Authorization", required=false) String authorization) throws InvalidTokenException, ResourceNotFoundException, EntitlementNotFoundException, LicenseConsumptionException, BadRequestException {
		String token = authorizationValidator.validateAndGetToken(sessionCookie, authorization);
		licensingService.consumeLicense(token, licenseRequest.getApplicationId());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Release license.
	 *
	 * @return the license release status.
	 */
	@ApiVersion("1")
	@RolesAllowed({ "EPO_APP.LIST" })
	@RequestMapping(value = "/$Release", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity releaseLicense(@RequestBody LicenseRequest licenseRequest,
										 @CookieValue(value = SESSION_COOKIE, required = false) String sessionCookie,
										 @RequestHeader(value="Authorization", required=false) String authorization) throws InvalidTokenException, ResourceNotFoundException, LicenseConsumptionException, EntitlementNotFoundException, BadRequestException {
		String token = authorizationValidator.validateAndGetToken(sessionCookie, authorization);
		licensingService.releaseLicense(token, licenseRequest.getApplicationId());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
