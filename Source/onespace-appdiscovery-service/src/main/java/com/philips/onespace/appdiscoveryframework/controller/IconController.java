/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: IconController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.philips.onespace.service.StorageService;
import com.philips.onespace.validator.ApiVersion;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/Icon")
public class IconController {
	
	@Autowired
	private StorageService storaService;
	
	@Value("${aws.iconbucketName}")
	private String iconBucketName;
	
	/**
	 * Save icon.
	 *
	 * @return Boolean, icon upload status.
	 */
	@ApiVersion("1")
	@RolesAllowed({ "EPO_APP.REGISTER", "EPO_APP.UPDATE" })
	@RequestMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Boolean> upload(@PathVariable("name") String name,
			@RequestPart(value = "file") MultipartFile file) throws IOException {
		return new ResponseEntity<>(storaService.uploadFile(file, iconBucketName, name), HttpStatus.OK);
	}

	/**
	 * Update icon.
	 *
	 * @return Boolean, icon upload status.
	 */
	@ApiVersion("1")
	@RolesAllowed({ "EPO_APP.UPDATE" })
	@RequestMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@PathVariable("name") String name,
										  @RequestPart(value = "file") MultipartFile file) throws IOException {
		return new ResponseEntity<>(storaService.uploadFile(file, iconBucketName, name), HttpStatus.OK);
	}
}
