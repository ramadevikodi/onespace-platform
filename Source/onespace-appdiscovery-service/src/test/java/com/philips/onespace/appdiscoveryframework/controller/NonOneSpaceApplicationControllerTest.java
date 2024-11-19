/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NonOneSpaceApplicationControllerTest.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.philips.onespace.appdiscoveryframework.service.interfaces.NonOneSpaceApplicationService;
import com.philips.onespace.dto.NonOneSpaceApplication;
import com.philips.onespace.dto.NonOneSpaceApplicationOrder;
import com.philips.onespace.exception.ResourceNotFoundException;

class NonOneSpaceApplicationControllerTest {

	@InjectMocks
	private NonOneSpaceApplicationController nononespaceApplicationController;

	@Mock
	private NonOneSpaceApplicationService service;

	@BeforeEach
	public void setUp() throws SQLException {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSave() throws Exception {
		// Arrange
		NonOneSpaceApplication application = getApplication();
		when(service.save(Mockito.any(), Mockito.any())).thenReturn(application);

		// Act
		ResponseEntity<NonOneSpaceApplication> result = nononespaceApplicationController.save(application);

		// Assert
		assertNotNull(result);
		assertEquals("testName", result.getBody().getName());
		assertEquals("testDescription", result.getBody().getDescription());
		assertEquals("testIcon", result.getBody().getIcon());
		assertEquals("active", result.getBody().getStatus());
		assertEquals("testUrl", result.getBody().getUrl());
		assertEquals("tetUser", result.getBody().getRegisteredBy());
		assertNotNull(result.getBody().getRegisteredDateTime());
	}

	@Test
	void testGetAll() throws Exception {
		// Arrange
		List<NonOneSpaceApplication> applicationList = new ArrayList<NonOneSpaceApplication>();
		NonOneSpaceApplication application = getApplication();
		applicationList.add(application);
		when(service.getAll()).thenReturn(applicationList);

		// Act
		ResponseEntity<List<NonOneSpaceApplication>> result = nononespaceApplicationController.getAll();

		// Assert
		assertNotNull(result);
		assertEquals("testName", result.getBody().get(0).getName());
		assertEquals("testDescription", result.getBody().get(0).getDescription());
		assertEquals("testIcon", result.getBody().get(0).getIcon());
		assertEquals("active", result.getBody().get(0).getStatus());
		assertEquals("testUrl", result.getBody().get(0).getUrl());
		assertEquals("tetUser", result.getBody().get(0).getRegisteredBy());
		assertNotNull(result.getBody().get(0).getRegisteredDateTime());
	}

	@Test
	void testgetById() throws Exception {
		// Arrange
		NonOneSpaceApplication application = getApplication();
		UUID appId = UUID.randomUUID();
		when(service.getById(Mockito.any())).thenReturn(application);

		// Act
		ResponseEntity<NonOneSpaceApplication> result = nononespaceApplicationController.getById(appId);

		// Assert
		assertNotNull(result);
		assertEquals("testName", result.getBody().getName());
		assertEquals("testDescription", result.getBody().getDescription());
		assertEquals("testIcon", result.getBody().getIcon());
		assertEquals("active", result.getBody().getStatus());
		assertEquals("testUrl", result.getBody().getUrl());
		assertEquals("tetUser", result.getBody().getRegisteredBy());
		assertNotNull(result.getBody().getRegisteredDateTime());
	}

	@Test
	void testupdateById() throws Exception {
		// Arrange
		NonOneSpaceApplication application = getApplication();
		UUID appId = UUID.randomUUID();

		// Act
		ResponseEntity<String> result = nononespaceApplicationController.updateById(appId, application);

		// Assert
		assertNotNull(result);
	}

	@Test
	void testupdateAll() throws Exception {
		// Arrange
		List<NonOneSpaceApplicationOrder> applicationOrders = new ArrayList<NonOneSpaceApplicationOrder>();
		NonOneSpaceApplicationOrder applicationOrder = new NonOneSpaceApplicationOrder();
		applicationOrder.setId(UUID.randomUUID());
		applicationOrder.setOrder(1);
		applicationOrders.add(applicationOrder);

		// Act
		ResponseEntity<String> result = nononespaceApplicationController.updateAll(applicationOrders);

		// Assert
		assertNotNull(result);
	}

	@Test
	void testdeleteById() throws BadRequestException, ParseException, ResourceNotFoundException {
		// Arrange
		NonOneSpaceApplication application = new NonOneSpaceApplication();
		UUID appId = UUID.randomUUID();
		when(service.save(Mockito.any(), Mockito.any())).thenReturn(application);

		// Act
		ResponseEntity<String> result = nononespaceApplicationController.deleteById(appId);

		// Assert
		assertNotNull(result);
	}

	public NonOneSpaceApplication getApplication() throws Exception {
		NonOneSpaceApplication application = new NonOneSpaceApplication();
		application.setName("testName");
		application.setDescription("testDescription");
		application.setOrder(1);
		application.setRegisteredBy("tetUser");
		application.setStatus("active");
		application.setUrl("testUrl");
		application.setIcon("testIcon");
		application.setRegisteredDateTime(new Date().toString());
		return application;
	}

}
