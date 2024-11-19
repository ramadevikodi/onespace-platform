/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: BusinessUnitRepository.java
 */

package com.philips.onespace.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.philips.onespace.jpa.entity.BusinessUnitEntity;

public interface BusinessUnitRepository extends JpaRepository<BusinessUnitEntity, UUID> {

	/**
	 * Find Business Unit based on hsp iam org id.
	 * 
	 * @param hspIamOrgId
	 * @return the Optional BusinessUnitEntity
	 */
    Optional<BusinessUnitEntity> findByBusinessUnitExtSystemEntity_HspIamOrgId(UUID hspIamOrgId);

    /**
	 * Find Business Unit based on name.
	 * 
	 * @param name
	 * @return the Optional BusinessUnitEntity
	 */
    Optional<BusinessUnitEntity> findByName(String name);

}
