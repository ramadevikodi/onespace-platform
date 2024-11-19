/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: CustomerRepository.java
 */

package com.philips.onespace.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.philips.onespace.jpa.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

	/**
	 * Find Customer based on hsp iam org id.
	 * 
	 * @param hspIamOrgId
	 * @return the Optional CustomerEntity
	 */
    Optional<CustomerEntity> findByCustomerExtSystemEntity_HspIamOrgId(UUID hspIamOrgId);

}
