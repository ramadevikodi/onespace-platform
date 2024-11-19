/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: LanguageRepository.java
 */

package com.philips.onespace.jpa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.philips.onespace.jpa.entity.LanguageEntity;

public interface LanguageRepository extends JpaRepository<LanguageEntity, UUID> {
	/**
	 * Find language code exist based on locale.
	 * 
	 * @param locale
	 * @return the boolean isLanguageExist using code
	 */
	boolean existsByLanguageCodeIgnoreCase(String locale);
}
