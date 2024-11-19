/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NonOneSpaceApplicationController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.philips.onespace.appdiscoveryframework.service.interfaces.NonOneSpaceApplicationService;
import com.philips.onespace.dto.NonOneSpaceApplication;
import com.philips.onespace.dto.NonOneSpaceApplicationOrder;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.validator.ApiVersion;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

@Validated
@RequestMapping("/NonOneSpaceApplication")
@RestController
public class NonOneSpaceApplicationController {

	@Autowired
	private NonOneSpaceApplicationService applicationService;

	@Value("${nononespaceapp.maxlimit}")
	private String maxlimit;

	@ApiVersion("1")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@RolesAllowed({ "ONESPACE_NON_ONESPACEAPP.REGISTER" })
	public ResponseEntity<NonOneSpaceApplication> save(@Valid @RequestBody NonOneSpaceApplication application)
			throws BadRequestException, ParseException {
		return new ResponseEntity<>(applicationService.save(application, maxlimit), HttpStatus.CREATED);
	}

	@ApiVersion("1")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@RolesAllowed({ "ONESPACE_NON_ONESPACEAPP.LIST" })
	public ResponseEntity<List<NonOneSpaceApplication>> getAll() {
		return new ResponseEntity<>(applicationService.getAll(), HttpStatus.OK);
	}

	@ApiVersion("1")
	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@RolesAllowed({ "ONESPACE_NON_ONESPACEAPP.LIST" })
	public ResponseEntity<NonOneSpaceApplication> getById(@PathVariable("id") UUID id)
			throws BadRequestException, ResourceNotFoundException {
		return new ResponseEntity<>(applicationService.getById(id), HttpStatus.OK);
	}

	@ApiVersion("1")
	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PATCH)
	@RolesAllowed({ "ONESPACE_NON_ONESPACEAPP.UPDATE" })
	public ResponseEntity<String> updateById(@PathVariable("id") UUID id,
			@Valid @RequestBody NonOneSpaceApplication application)
			throws BadRequestException, ResourceNotFoundException {
		applicationService.updateById(id, application);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiVersion("1")
	@RequestMapping(value = "/$order", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@RolesAllowed({ "ONESPACE_NON_ONESPACEAPP.UPDATE" })
	public ResponseEntity<String> updateAll(@Valid @RequestBody List<NonOneSpaceApplicationOrder> applicationOrders)
			throws BadRequestException {
		applicationService.updateAll(applicationOrders);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiVersion("1")
	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	@RolesAllowed({ "ONESPACE_NON_ONESPACEAPP.DELETE" })
	public ResponseEntity<String> deleteById(@PathVariable("id") UUID id)
			throws BadRequestException, ResourceNotFoundException {
		applicationService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
