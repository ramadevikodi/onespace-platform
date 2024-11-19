/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ApplicationListingApiController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import static com.philips.onespace.util.Constants.SESSION_COOKIE;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.philips.onespace.appdiscoveryframework.mapper.ApplicationMapper;
import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.AppListingService;
import com.philips.onespace.appdiscoveryframework.util.validation.AuthorizationValidator;
import com.philips.onespace.dto.Application;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.util.Constants;
import com.philips.onespace.validator.ApiVersion;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/Application")
public class ApplicationListingApiController {

	@Autowired
	private AppListingService appListingService;

	@Autowired
	private ApplicationMapper applicationMapper;

	@Autowired
	private RateLimitService rateLimitService;
	
	@Autowired
	private AuthorizationValidator authorizationValidator;

	@ApiVersion("1")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@RolesAllowed({ "EPO_APP.LIST" })
	public ResponseEntity<List<Application>> listApplications(@CookieValue(value = SESSION_COOKIE, required = false) String sessionCookie,
															  @RequestHeader(value="Authorization", required=false) String authorization,
															  @RequestParam(value="status", required=false) UUID statusId,
															  @RequestParam(defaultValue = "lastModifiedDateTime") String sortBy,
															  @RequestParam(defaultValue = "DESC") String sortDir,
															  Pageable pageable)
			throws ResourceNotFoundException, InvalidTokenException, BadRequestException {
		String token = authorizationValidator.validateAndGetToken(sessionCookie, authorization);
		Sort sort = Sort.by(Sort.Order.by(sortBy).with(Sort.Direction.valueOf(sortDir)));
		if (pageable == null) {
			pageable = PageRequest.of(0, 20, sort);
		}
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		Page<ApplicationEntity> applicationPage = appListingService.listApplications(statusId, token, null, pageable);
		if (rateLimitService.consume()) {
			if(applicationPage.isEmpty()){
				return ResponseEntity.ok(Collections.emptyList());
			}
			else {
				HttpHeaders headers = createPaginationHeaders(applicationPage);
				return new ResponseEntity<>(applicationMapper.entityToDto(applicationPage), headers, HttpStatus.OK);
			}
		} else
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
	}

	@ApiVersion("1")
	@GetMapping("/$filter")
	@RolesAllowed( {"EPO_APP.LIST"} )
	public ResponseEntity<List<Application>> filterApplications(@CookieValue(value = SESSION_COOKIE, required = false) String sessionCookie,
																@RequestHeader(value="Authorization", required=false) String authorization,
																@RequestParam(value="status", required=false) UUID statusId,
																@RequestParam(required = false) Map<String, String> criteria,
																@RequestParam(defaultValue = "name") String sortBy,
																@RequestParam(defaultValue = "ASC") String sortDir,
																Pageable pageable) throws InvalidTokenException, ResourceNotFoundException, BadRequestException {
		String token = authorizationValidator.validateAndGetToken(sessionCookie, authorization);
		Sort sort = Sort.by(Sort.Order.by(sortBy).with(Sort.Direction.fromString(sortDir)));
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		Page<ApplicationEntity> applicationPage = appListingService.listApplications(statusId, token, criteria, pageable);
		if(applicationPage.isEmpty()){
			return ResponseEntity.ok(Collections.emptyList());
		}
		HttpHeaders headers = createPaginationHeaders(applicationPage);
		return new ResponseEntity<>(applicationMapper.entityToDto(applicationPage), headers, HttpStatus.OK);
	}

	private static HttpHeaders createPaginationHeaders(Page<ApplicationEntity> applicationPage) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.PAGINATION_HEADER_PAGE, String.valueOf(applicationPage.getNumber()));
		headers.add(Constants.PAGINATION_HEADER_SIZE, String.valueOf(applicationPage.getSize()));
		headers.add(Constants.PAGINATION_HEADER_SORT, String.valueOf(applicationPage.getSort()));
		headers.add(Constants.PAGINATION_HEADER_TOTAL_PAGES, String.valueOf(applicationPage.getTotalPages()));
		headers.add(Constants.PAGINATION_HEADER_TOTAL_RECORDS, String.valueOf(applicationPage.getTotalElements()));
		return headers;
	}
	
}