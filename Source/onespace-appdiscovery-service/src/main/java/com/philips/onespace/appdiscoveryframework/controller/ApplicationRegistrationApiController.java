/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ApplicationRegistrationApiController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.philips.onespace.appdiscoveryframework.mapper.ApplicationMapper;
import com.philips.onespace.appdiscoveryframework.service.AppRegistrationServiceImpl;
import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.appdiscoveryframework.util.SecurityContextUtil;
import com.philips.onespace.dto.Application;
import com.philips.onespace.dto.ApplicationID;
import com.philips.onespace.dto.EmailNotification;
import com.philips.onespace.dto.EmailNotificationType;
import com.philips.onespace.exception.DatabaseConstraintViolationException;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceExistsException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.notification.service.EmailNotificationService;
import com.philips.onespace.service.IamService;
import com.philips.onespace.util.Constants;
import com.philips.onespace.validator.ApiVersion;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/Application")
public class ApplicationRegistrationApiController {

	@Autowired
	private AppRegistrationServiceImpl appRegistrationService;

	@Autowired
	private ApplicationMapper applicationMapper;

	@Autowired
	private RateLimitService rateLimitService;
	@Autowired
	private EmailNotificationService emailNotificationService;
	@Autowired
	private SecurityContextUtil securityContextUtil;
	
	@Autowired
	private IamService iamService;
	
	@ApiVersion("1")
	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@RolesAllowed({ "EPO_APP.LIST" })
	public ResponseEntity<Application> getApplication(@PathVariable("id") UUID id) throws ResourceNotFoundException, BadRequestException {
		ApplicationEntity applicationEntity = appRegistrationService.getApplication(id);
		Application application = applicationMapper.entityToDto(applicationEntity);
		//Add group, user information to Application DTO
		if(null != application.getRegistration().getBusinessUnit())
			application.setGroups(iamService.getGroups(String.valueOf(application.getRegistration().getBusinessUnit().getHspIamOrgId())));
		return new ResponseEntity<>(application, HttpStatus.OK);
	}

	@ApiVersion("1")
	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
			"application/json" }, method = RequestMethod.PATCH)
	@RolesAllowed({ "EPO_APP.UPDATE", "EPO_APP.L2.APPROVE", "EPO_APP.L1.APPROVE" }) // TODO:: Update with L1 and L2
	public ResponseEntity<ApplicationID> patchApplication(@PathVariable("id") UUID id, @Valid @RequestBody Application application)
			throws ResourceNotFoundException, DatabaseConstraintViolationException, ResourceExistsException,
			BadRequestException {
		Application updatedApplication = appRegistrationService.updateApplication(id, application);
		sendEmailNotification(updatedApplication, getEmailNotificationType(updatedApplication));
		if (rateLimitService.consume()) {
			ApplicationID applicationID = ApplicationID.builder().id(updatedApplication.getId()).build();
			return new ResponseEntity<>(applicationID, HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		}
	}

	@ApiVersion("1")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@RolesAllowed("EPO_APP.REGISTER")
	public ResponseEntity<ApplicationID> registerApplication(@Valid @RequestBody Application application, BindingResult bindingResult)
			throws DatabaseConstraintViolationException, InvalidTokenException, ResourceExistsException,
			BadRequestException {
		Application applicationCreated = appRegistrationService.registerApplication(application);
		ApplicationID applicationID = ApplicationID.builder().id(applicationCreated.getId()).build();
		sendEmailNotification(applicationCreated, EmailNotificationType.APP_REGISTERED);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{applicationID}")
				.buildAndExpand(applicationID).toUri();

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setLocation(location);
		if (rateLimitService.consume()) {
			return new ResponseEntity<>(applicationID, responseHeaders, HttpStatus.CREATED);
		} else {
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		}
	}

	// TODO:: Invoke this method asynchronously
	private void sendEmailNotification(Application application, EmailNotificationType emailNotificationType) {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("appName", application.getRegistration().getName());
		metadata.put("appDescription", application.getRegistration().getShortDescription());
		metadata.put("appIcon", null != application.getDeployment() ? application.getDeployment().getIcon() : null);
		metadata.put("appLastModifiedDateTime", String.valueOf(application.getLastModifiedDateTime()));
		EmailNotification emailNotification = EmailNotification.builder()
				.type(emailNotificationType)
				.to(List.of(securityContextUtil.getUserNameFromPrincipal(SecurityContextHolder.getContext().getAuthentication().getPrincipal())))
				// TODO:: Add cc list
				.metadata(metadata)
				.build();
		emailNotificationService.sendEmailNotification(emailNotification, true);
	}

	private EmailNotificationType getEmailNotificationType(Application updatedApplication) {
		EmailNotificationType emailNotificationType = null;
		if(Objects.equals(updatedApplication.getStatus().getName(), Constants.AWAITING_MARKET_SOLUTION_OWNER_APPROVAL)) {
			emailNotificationType = EmailNotificationType.APP_BUSINESS_OWNER_APPROVED;
		} else if(Objects.equals(updatedApplication.getStatus().getName(), Constants.SOLUTION_OWNER_APPROVED)) {
			emailNotificationType = EmailNotificationType.APP_SOLUTION_OWNER_APPROVED;
		}
		return emailNotificationType;
	}
	
}
