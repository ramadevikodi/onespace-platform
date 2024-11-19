/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ApplicationStatusRepository.java
 */

package com.philips.onespace.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.philips.onespace.jpa.entity.ApplicationStatusEntity;


public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatusEntity, UUID> {

	/**
	 * Find application status based on name.
	 * 
	 * @param name
	 * @return the Optional ApplicationStatusEntity
	 */
    Optional<ApplicationStatusEntity> findByName(String name);
}
