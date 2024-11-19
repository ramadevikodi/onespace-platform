/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: UserMarketMappingRepository.java
 */

package com.philips.onespace.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.philips.onespace.jpa.entity.UserMarketMapEntity;
import com.philips.onespace.jpa.entity.UserMarketMapID;

public interface UserMarketMappingRepository extends ListCrudRepository<UserMarketMapEntity, UserMarketMapID> {
    
	/**
	 * Find UserMarketMap based on hsp user uuid.
	 * 
	 * @param hspUserUuid
	 * @return the List UserMarketMapEntity
	 */
	List<UserMarketMapEntity> findAllByHspUserUuid(UUID hspUserUuid);
}
