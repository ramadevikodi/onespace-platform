/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: RdbmsBasedNonOneSpaceApplicationServiceTest.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import com.philips.onespace.appdiscoveryframework.mapper.NonOneSpaceApplicationMapper;
import com.philips.onespace.appdiscoveryframework.util.SecurityContextUtil;
import com.philips.onespace.dto.NonOneSpaceApplication;
import com.philips.onespace.dto.NonOneSpaceApplicationOrder;
import com.philips.onespace.dto.SecurityContextDetail;
import com.philips.onespace.jpa.entity.CustomerExtSystemEntity;
import com.philips.onespace.jpa.entity.NonOneSpaceApplicationEntity;
import com.philips.onespace.jpa.repository.CustomerExtSystemRepository;
import com.philips.onespace.jpa.repository.NonOneSpaceApplicationRepository;

@TestPropertySource(properties = {
	    "nononespaceapp.maxlimit=12",
	})
public class RdbmsBasedNonOneSpaceApplicationServiceTest {

	@InjectMocks
	private RdbmsBasedNonOneSpaceApplicationService service;

	@Mock
	private NonOneSpaceApplicationRepository nononespaceApplicationRepository;
	
	@Mock
	private CustomerExtSystemRepository customerExtSystemRepository;
	
	@Mock
	private SecurityContextUtil securityContextUtil;

	@Mock
	private NonOneSpaceApplicationMapper mapper;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		String loggedInUserId = "7583f5f4-ea78-442a-99f4-0caddd45fb19";
		SecurityContext securityContext = mock(SecurityContext.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContextDetail principalObject = mock(SecurityContextDetail.class);
		SecurityContextHolder.setContext(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(principalObject);
		when(principalObject.getUserId()).thenReturn(loggedInUserId);
		when(principalObject.getUserName()).thenReturn("testUser");
		when(principalObject.getManagingOrganization()).thenReturn("7583f5f4-ea78-442a-99f4-0caddd45fb19");
	}

	@Test
	void testSave() throws Exception {
		// Arrange
		NonOneSpaceApplication application = getApplication();
		NonOneSpaceApplicationEntity applicationEntity = getApplicationEntity();
		when(nononespaceApplicationRepository.save(Mockito.any())).thenReturn(applicationEntity);
		when(nononespaceApplicationRepository.countAppsByCustomerId(Mockito.any())).thenReturn(12L);
		CustomerExtSystemEntity customerExtSystemEntity = new CustomerExtSystemEntity();
		customerExtSystemEntity.setId(UUID.fromString("9d838ac3-8d0c-4c65-b9b1-9308a0b81ee1"));
		customerExtSystemEntity.setHspIamOrgId(UUID.fromString("c169a945-8c28-40ec-bc77-98275c585993"));
		Optional<CustomerExtSystemEntity> custExtSysOpt = Optional.of(customerExtSystemEntity);
		when(customerExtSystemRepository.findByHspIamOrgId(Mockito.any())).thenReturn(custExtSysOpt);
		when(securityContextUtil.getUserNameFromPrincipal(Mockito.any())).thenReturn("testUser");
		when(mapper.dtoToEntity(Mockito.any())).thenReturn(applicationEntity);
		when(mapper.entityToDto(Mockito.any())).thenReturn(application);

		// Act
		NonOneSpaceApplication response = service.save(application, "50");

		// Assert
		assertNotNull(response);
		assertEquals("testName", response.getName());
		assertEquals("testDescription", response.getDescription());
		assertEquals("testIcon", response.getIcon());
		assertEquals("active", response.getStatus());
		assertEquals("testUrl", response.getUrl());
		assertEquals("tetUser", response.getRegisteredBy());
		assertNotNull(response.getRegisteredDateTime());
	}

	@Test
	void testGetAll() throws Exception {
		// Arrange
		NonOneSpaceApplication application = getApplication();
		NonOneSpaceApplicationEntity applicationEntity = getApplicationEntity();
		List<NonOneSpaceApplicationEntity> applicationEntityList = new ArrayList<NonOneSpaceApplicationEntity>();
		applicationEntityList.add(applicationEntity);
		Optional<List<NonOneSpaceApplicationEntity>> optEntity = Optional.of(applicationEntityList);
		when(nononespaceApplicationRepository.findByUserManOrgOrderByOrderAsc(Mockito.any())).thenReturn(optEntity);
		when(mapper.dtoToEntity(Mockito.any())).thenReturn(applicationEntity);
		when(mapper.entityToDto(Mockito.any())).thenReturn(application);

		// Act
		List<NonOneSpaceApplication> response = service.getAll();

		// Assert
		assertNotNull(application);
		assertEquals("testName", response.get(0).getName());
		assertEquals("testDescription", response.get(0).getDescription());
		assertEquals("testIcon", response.get(0).getIcon());
		assertEquals("active", response.get(0).getStatus());
		assertEquals("testUrl", response.get(0).getUrl());
		assertEquals("tetUser", response.get(0).getRegisteredBy());
	}

	@Test
	void testGetById() throws Exception {
		// Arrange
		NonOneSpaceApplication application = getApplication();
		NonOneSpaceApplicationEntity applicationEntity = getApplicationEntity();
		Optional<NonOneSpaceApplicationEntity> optEntity = Optional.of(applicationEntity);
		when(nononespaceApplicationRepository.findByIdAndUserManOrg(Mockito.any(), Mockito.any())).thenReturn(optEntity);
		when(mapper.dtoToEntity(Mockito.any())).thenReturn(applicationEntity);
		when(mapper.entityToDto(Mockito.any())).thenReturn(application);

		// Act
		NonOneSpaceApplication response = service.getById(application.getId());

		// Assert
		assertNotNull(response);
		assertEquals("testName", response.getName());
		assertEquals("testDescription", response.getDescription());
		assertEquals("testIcon", response.getIcon());
		assertEquals("active", response.getStatus());
		assertEquals("testUrl", response.getUrl());
		assertEquals("tetUser", response.getRegisteredBy());
	}

	@Test
	void testUpdateById() throws Exception {
		// Arrange
		NonOneSpaceApplication application = getApplication();
		NonOneSpaceApplicationEntity applicationEntity = getApplicationEntity();
		Optional<NonOneSpaceApplicationEntity> optEntity = Optional.of(applicationEntity);
		when(nononespaceApplicationRepository.findByIdAndUserManOrg(Mockito.any(), Mockito.any())).thenReturn(optEntity);
		when(nononespaceApplicationRepository.save(Mockito.any())).thenReturn(applicationEntity);
		when(mapper.dtoToEntity(Mockito.any())).thenReturn(applicationEntity);
		when(mapper.entityToDto(Mockito.any())).thenReturn(application);

		// Act
		service.updateById(application.getId(), application);

		// Assert
		assertNotNull(application);
	}

	@Test
	void testUpdateAll() throws Exception {
		// Arrange
		NonOneSpaceApplicationOrder applicationOrder = new NonOneSpaceApplicationOrder();
		applicationOrder.setId(UUID.randomUUID());
		applicationOrder.setOrder(1);
		List<NonOneSpaceApplicationOrder> applicationOrders = new ArrayList<NonOneSpaceApplicationOrder>();
		applicationOrders.add(applicationOrder);
		NonOneSpaceApplication application = getApplication();
		NonOneSpaceApplicationEntity applicationEntity = getApplicationEntity();
		Mockito.doNothing().when(nononespaceApplicationRepository).updateOrderById(Mockito.any(), Mockito.any());
		when(nononespaceApplicationRepository.save(Mockito.any())).thenReturn(applicationEntity);
		when(mapper.dtoToEntity(Mockito.any())).thenReturn(applicationEntity);
		when(mapper.entityToDto(Mockito.any())).thenReturn(application);

		// Act
		service.updateAll(applicationOrders);

		// Assert
		assertNotNull(applicationOrders);
	}

	@Test
	void testDelete() throws Exception {
		// Arrange
		NonOneSpaceApplication application = getApplication();
		application.setId(UUID.randomUUID());
		NonOneSpaceApplicationEntity applicationEntity = getApplicationEntity();
		Optional<NonOneSpaceApplicationEntity> optEntity = Optional.of(applicationEntity);
		when(nononespaceApplicationRepository.findByIdAndUserManOrg(Mockito.any(), Mockito.any())).thenReturn(optEntity);
		Mockito.doNothing().when(nononespaceApplicationRepository).deleteById(Mockito.any());
		when(nononespaceApplicationRepository.save(Mockito.any())).thenReturn(applicationEntity);
		when(mapper.dtoToEntity(Mockito.any())).thenReturn(applicationEntity);
		when(mapper.entityToDto(Mockito.any())).thenReturn(application);

		// Act
		service.delete(application.getId());

		// Assert
		assertNotNull(application.getId());
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

	public NonOneSpaceApplicationEntity getApplicationEntity() throws Exception {
		NonOneSpaceApplicationEntity applicationEntity = new NonOneSpaceApplicationEntity();
		applicationEntity.setName("testName");
		applicationEntity.setDescription("testDescription");
		applicationEntity.setOrder(1);
		applicationEntity.setRegisteredBy("tetUser");
		applicationEntity.setStatus("active");
		applicationEntity.setUrl("testUrl");
		applicationEntity.setIcon("testIcon");
		return applicationEntity;
	}

}
