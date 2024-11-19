/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: EmailNotificationController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

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

import com.philips.onespace.dto.EmailNotification;
import com.philips.onespace.exception.UndeliveredMailException;
import com.philips.onespace.notification.service.EmailNotificationService;
import com.philips.onespace.util.Constants;
import com.philips.onespace.util.ErrorMessages;
import com.philips.onespace.validator.ApiVersion;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

@Validated
@RequestMapping("/Notification/Email")
@RestController
public class EmailNotificationController {
	private EmailNotificationService emailNotificationService;
	
	@Autowired
	public EmailNotificationController(EmailNotificationService emailNotificationService) {
		this.emailNotificationService = emailNotificationService;
	}

	/**
	 * Send email notification.
	 *
	 * @param emailNotification The email notification details
	 * @return Success or failure response
	 */
	@ApiVersion("1")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@RolesAllowed({ "EPO_APP.LIST" })
	public ResponseEntity sendEmailNotification(@Valid @RequestBody EmailNotification emailNotification) throws Exception {
		boolean sendTemplatedEmail = true;
		if(emailNotification.getType() == null) {
			sendTemplatedEmail = false;
			if(emailNotification.getSubject() == null || emailNotification.getMessage() == null) {
				throw new BadRequestException(ErrorMessages.MISSING_EMAIL_REQUEST_PARAM_ERR_CODE);
			}
		}
		boolean isEmailSent = emailNotificationService.sendEmailNotification(emailNotification, sendTemplatedEmail);
		if(!isEmailSent) {
			throw new UndeliveredMailException(Constants.EMAIL_NOT_SENT_ERR_CODE);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
