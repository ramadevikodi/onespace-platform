/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ApplicationRepository.java
 */

package com.philips.onespace.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.philips.onespace.jpa.entity.ApplicationEntity;

import jakarta.persistence.Tuple;


public interface ApplicationRepository extends JpaRepository<ApplicationEntity, UUID>, JpaSpecificationExecutor<ApplicationEntity> {
    
	/**
	 * Find applications based on owner organization.
	 * 
	 * @param ownerOrganization
	 * @return the List ApplicationEntity
	 */
	List<ApplicationEntity> findAllByOwnerOrganization(UUID ownerOrganization);
	
	/**
	 * Find all applications based on owner organization and statusId.
	 * 
	 * @param ownerOrganization
	 * @param statusId
	 * @return the List ApplicationEntity
	 */
    List<ApplicationEntity> findAllByOwnerOrganizationAndStatusId(UUID ownerOrganization, UUID statusId);
    
    /**
	 * Find applications based on statusId.
	 * 
	 * @param statusId
	 * @param pageable
	 * @return the List ApplicationEntity
	 */
    Page<ApplicationEntity> findAllByStatusId(UUID statusId, Pageable pageable);

    /**
	 * Find applications based on name.
	 * 
	 * @param name
	 * @return the List ApplicationEntity
	 */
    Optional<ApplicationEntity> findByNameIgnoreCase(String name);

    /**
	 * Find applications based in name.
	 * 
	 * @param name
	 * @return the List ApplicationEntity
	 */
    List<ApplicationEntity> findByNameIn(List<String> name);

    /**
	 * Find applications based in category name.
	 * 
	 * @param categoryName
	 * @return the List ApplicationEntity
	 */
    List<ApplicationEntity> findAllByCategoryName(String categoryName);

    /**
	 * Find applications based in category market id.
	 * 
	 * @param marketId
	 * @return the List ApplicationEntity
	 */
    List<ApplicationEntity> findByMarkets_MarketIdAndPublishedDateTimeIsNotNull(UUID marketId);

    /**
	 * Find application based on name and version.
	 * 
	 * @param name
	 * @param version
	 * @return the List ApplicationEntity
	 */
    Optional<ApplicationEntity> findByNameIgnoreCaseAndVersion(String name, String version);
    @Query(nativeQuery = true, value = "SELECT * FROM get_application_translations(:record_id, :language_code)")
    List<String> getApplicationTranslations(
            @Param("record_id") UUID recordId,
            @Param("language_code") String languageCode
    );

    /**
	 * Find filter criteria Integrator using sql function.
	 * 
	 * @return the FilterCriteria Tuple
	 */
    @Query(nativeQuery = true, value = "SELECT * FROM get_filter_criteria_for_app_integrator()")
    List<Tuple> getFilterCriteriaForAppIntegrator();

    /**
	 * Find filter criteria KAM using sql function.
	 * 
	 * @return the FilterCriteria Tuple
	 */
    @Query(nativeQuery = true, value = "SELECT * FROM get_filter_criteria_for_kam()")
    List<Tuple> getFilterCriteriaForKAM();

    /**
	 * Find filter criteria EndUser using sql function.
	 * 
	 * @return the FilterCriteria Tuple
	 */
    @Query(nativeQuery = true, value = "SELECT * FROM get_filter_criteria_for_hospital_end_user()")
    List<Tuple> getFilterCriteriaForHospitalEndUser();

	/**
	 * Find filter criteria PlatformAdmin using sql function.
	 *
	 * @return the FilterCriteria Tuple
	 */
	@Query(nativeQuery = true, value = "SELECT * FROM get_filter_criteria_for_platform_admin()")
	List<Tuple> getFilterCriteriaForPlatformAdmin();

	/**
	 * Find Third Party Applications.
	 * @param isThirdParty boolean
	 * @return the List ApplicationEntity
	 */
    List<ApplicationEntity> findByIsThirdParty(boolean isThirdParty);
}
