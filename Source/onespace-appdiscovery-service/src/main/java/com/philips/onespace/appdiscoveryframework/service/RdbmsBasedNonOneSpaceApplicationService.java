/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: RdbmsBasedNonOneSpaceApplicationService.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.mapper.NonOneSpaceApplicationMapper;
import com.philips.onespace.appdiscoveryframework.service.interfaces.NonOneSpaceApplicationService;
import com.philips.onespace.appdiscoveryframework.util.SecurityContextUtil;
import com.philips.onespace.dto.NonOneSpaceApplication;
import com.philips.onespace.dto.NonOneSpaceApplicationOrder;
import com.philips.onespace.dto.SecurityContextDetail;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.CustomerExtSystemEntity;
import com.philips.onespace.jpa.entity.NonOneSpaceApplicationEntity;
import com.philips.onespace.jpa.repository.CustomerExtSystemRepository;
import com.philips.onespace.jpa.repository.NonOneSpaceApplicationRepository;
import com.philips.onespace.util.DateUtil;
import com.philips.onespace.util.ErrorMessages;

@Service
public class RdbmsBasedNonOneSpaceApplicationService implements NonOneSpaceApplicationService {

	@Autowired
	private NonOneSpaceApplicationMapper mapper;

	@Autowired
	private NonOneSpaceApplicationRepository repository;

	@Autowired
	private SecurityContextUtil securityContextUtil;

	@Autowired
	private CustomerExtSystemRepository customerExtSystemRepository;

	@Override
	public NonOneSpaceApplication save(NonOneSpaceApplication application, String maxlimit)
			throws BadRequestException, ParseException {
		NonOneSpaceApplication response = null;
		SecurityContextDetail principalObject = (SecurityContextDetail) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String userManagingOrg = principalObject.getManagingOrganization();
		Optional<CustomerExtSystemEntity> custExtSysOpt = customerExtSystemRepository
				.findByHspIamOrgId(UUID.fromString(userManagingOrg));
		if (!custExtSysOpt.isPresent()) {
			throw new BadRequestException(ErrorMessages.NOTFOUND_IAMORG_ID);
		}
		long appCount = repository.countAppsByCustomerId(custExtSysOpt.get().getId());
		if (appCount >= Long.parseLong(maxlimit)) {
			throw new BadRequestException(ErrorMessages.MAX_LIMIT_NONONESPACEAPPLICATION);
		}
		NonOneSpaceApplicationEntity entity = mapper.dtoToEntity(application);
		entity.setRegisteredBy(securityContextUtil
				.getUserNameFromPrincipal(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
		entity.setRegisteredDateTime(DateUtil.formatDateTime(DateUtil.getCurrentDateUTC()));
		entity.setCustomerId(custExtSysOpt.get().getId());
		NonOneSpaceApplicationEntity entitySaved = repository.save(entity);
		response = mapper.entityToDto(entitySaved);
		return response;
	}

	@Override
	public List<NonOneSpaceApplication> getAll() {
		List<NonOneSpaceApplication> responseList = new ArrayList<NonOneSpaceApplication>();
		SecurityContextDetail principalObject = (SecurityContextDetail) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String userManagingOrg = principalObject.getManagingOrganization();
		Optional<List<NonOneSpaceApplicationEntity>> entitiesOpt = repository
				.findByUserManOrgOrderByOrderAsc(UUID.fromString(userManagingOrg));
		if (entitiesOpt.isPresent()) {
			responseList = entitiesOpt.get().stream()
					.map(nononespaceApplicationEntity -> mapper.entityToDto(nononespaceApplicationEntity))
					.collect(Collectors.toList());
		}
		return responseList;
	}

	@Override
	public NonOneSpaceApplication getById(UUID applicationId) throws ResourceNotFoundException {
		NonOneSpaceApplication response = null;
		SecurityContextDetail principalObject = (SecurityContextDetail) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String userManagingOrg = principalObject.getManagingOrganization();
		Optional<NonOneSpaceApplicationEntity> entityOpt = repository.findByIdAndUserManOrg(applicationId, UUID.fromString(userManagingOrg));
		if (!entityOpt.isPresent()) {
			throw new ResourceNotFoundException(ErrorMessages.NOTFOUND_NONONESPACEAPPLICATION_ID);
		}
		response = mapper.entityToDto(entityOpt.get());
		return response;
	}

	@Override
	public void updateById(UUID applicationId, NonOneSpaceApplication application) throws ResourceNotFoundException {
		SecurityContextDetail principalObject = (SecurityContextDetail) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String userManagingOrg = principalObject.getManagingOrganization();
		Optional<NonOneSpaceApplicationEntity> entityOpt = repository.findByIdAndUserManOrg(applicationId, UUID.fromString(userManagingOrg));
		if (!entityOpt.isPresent()) {
			throw new ResourceNotFoundException(ErrorMessages.NOTFOUND_NONONESPACEAPPLICATION_ID);
		}
		NonOneSpaceApplicationEntity entityFetched = entityOpt.get();
		NonOneSpaceApplicationEntity entity = new NonOneSpaceApplicationEntity();
		entity.setId(entityFetched.getId());
		entity.setName(null != application.getName() ? application.getName() : entityFetched.getName());
		entity.setDescription(
				null != application.getDescription() ? application.getDescription() : entityFetched.getDescription());
		entity.setUrl(null != application.getUrl() ? application.getUrl() : entityFetched.getUrl());
		entity.setIcon(null != application.getIcon() ? application.getIcon() : entityFetched.getIcon());
		entity.setStatus(null != application.getStatus() ? application.getStatus() : entityFetched.getStatus());
		entity.setOrder(null != application.getOrder() ? application.getOrder() : entityFetched.getOrder());
		entity.setRegisteredBy(entityFetched.getRegisteredBy());
		entity.setRegisteredDateTime(entityFetched.getRegisteredDateTime());
		entity.setCustomerId(entityFetched.getCustomerId());
		repository.save(entity);
	}

	@Override
	public void updateAll(List<NonOneSpaceApplicationOrder> applicationOrders) {
		for (NonOneSpaceApplicationOrder nononespaceApplicationOrder : applicationOrders) {
			repository.updateOrderById(nononespaceApplicationOrder.getId(), nononespaceApplicationOrder.getOrder());
		}
	}

	@Override
	public void delete(UUID applicationId) throws ResourceNotFoundException {
		SecurityContextDetail principalObject = (SecurityContextDetail) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String userManagingOrg = principalObject.getManagingOrganization();
		Optional<NonOneSpaceApplicationEntity> entityOpt = repository.findByIdAndUserManOrg(applicationId, UUID.fromString(userManagingOrg));
		if (!entityOpt.isPresent()) {
			throw new ResourceNotFoundException(ErrorMessages.NOTFOUND_NONONESPACEAPPLICATION_ID);
		}
		repository.deleteById(applicationId);
	}

}
