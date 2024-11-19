/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ApplicationFilterApiController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import static com.philips.onespace.util.Constants.SESSION_COOKIE;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.philips.onespace.appdiscoveryframework.service.FilterCriteriaService;
import com.philips.onespace.appdiscoveryframework.util.validation.AuthorizationValidator;
import com.philips.onespace.dto.ApplicationFilter;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.validator.ApiVersion;

import jakarta.annotation.security.RolesAllowed;

@Validated
@RequestMapping("/ApplicationFilterCriteria")
@RestController
public class ApplicationFilterApiController {

	@Autowired
	private FilterCriteriaService filterCriteriaService;

	@Autowired
	private AuthorizationValidator authorizationValidator;

	@ApiVersion("1")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@RolesAllowed({ "EPO_APP.LIST" })
	public ResponseEntity<List<ApplicationFilter>> getFilterCriteria(
			@CookieValue(value = SESSION_COOKIE, required = false) String sessionCookie,
			@RequestHeader(value = "Authorization", required = false) String authorization)
			throws InvalidTokenException, BadRequestException {
		String token = 	authorizationValidator.validateAndGetToken(sessionCookie, authorization);
		List<ApplicationFilter> criteria = filterCriteriaService.getCriteriaDataForCurrentUser(token);
		return new ResponseEntity<>(criteria, HttpStatus.OK);
	}
}
