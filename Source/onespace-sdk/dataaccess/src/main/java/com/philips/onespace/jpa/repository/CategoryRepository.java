/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: CategoryRepository.java
 */

package com.philips.onespace.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.philips.onespace.jpa.entity.CategoryEntity;


public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

	/**
	 * Find Category based on category.
	 * 
	 * @param category
	 * @return the Optional CategoryEntity
	 */
    Optional<CategoryEntity> findByName(String category);

}
