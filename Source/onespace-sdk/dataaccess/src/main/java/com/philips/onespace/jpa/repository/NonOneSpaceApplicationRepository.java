/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NonOneSpaceApplicationRepository.java
 */

package com.philips.onespace.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.philips.onespace.jpa.entity.NonOneSpaceApplicationEntity;

import jakarta.transaction.Transactional;

public interface NonOneSpaceApplicationRepository extends JpaRepository<NonOneSpaceApplicationEntity, UUID> {

	/**
	 * This method gets all the non onespace applications for the given user managing org.
	 *
	 * @param userManagingOrg
	 * @return NonOneSpaceApplicationEntity optional list
	 */
	@Query("FROM NonOneSpaceApplicationEntity noa WHERE noa.customerEntity.customerExtSystemEntity.hspIamOrgId=:userManagingOrg ORDER BY noa.order ASC")
	Optional<List<NonOneSpaceApplicationEntity>> findByUserManOrgOrderByOrderAsc(UUID userManagingOrg);
	
	/**
	 * This method gets the non onespace application for the given user managing org.
	 *
	 * @param id
	 * @param userManagingOrg
	 * @return NonOneSpaceApplicationEntity 
	 */
	@Query("FROM NonOneSpaceApplicationEntity noa WHERE noa.id=:id and noa.customerEntity.customerExtSystemEntity.hspIamOrgId=:userManagingOrg")
	Optional<NonOneSpaceApplicationEntity> findByIdAndUserManOrg(UUID id, UUID userManagingOrg);
	
	/**
	 * This method updates the non onespace application order.
	 *
	 * @param id
	 * @param order
	 */
	@Modifying
	@Transactional
	@Query("UPDATE NonOneSpaceApplicationEntity noa SET noa.order=:order WHERE noa.id=:id")
	void updateOrderById(UUID id, Integer order);
	
	/**
	 * This method gets the non onespace application detail for the given customer id.
	 *
	 * @param customerId
	 * @return count, the application count 
	 */
	@Query("SELECT COUNT(noa) FROM NonOneSpaceApplicationEntity noa WHERE noa.customerId=:customerId")
	long countAppsByCustomerId(UUID customerId);
}
