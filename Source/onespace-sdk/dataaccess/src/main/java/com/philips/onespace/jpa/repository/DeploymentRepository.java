/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: DeploymentRepository.java
 */

package com.philips.onespace.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.philips.onespace.jpa.entity.DeploymentEntity;

public interface DeploymentRepository extends CrudRepository<DeploymentEntity, UUID> {
	/**
	 * Find Deployment based on mode.
	 * 
	 * @param mode
	 * @return the Optional DeploymentEntity
	 */
	List<DeploymentEntity> findAll(Sort mode);
}
