/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: BusinessUnitServiceImpl.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.mapper.BusinessUnitMapper;
import com.philips.onespace.appdiscoveryframework.service.interfaces.BusinessUnitService;
import com.philips.onespace.dto.BusinessUnit;
import com.philips.onespace.jpa.entity.BusinessUnitEntity;
import com.philips.onespace.jpa.repository.BusinessUnitRepository;


@Service
public class BusinessUnitServiceImpl implements BusinessUnitService {

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private BusinessUnitMapper businessUnitMapper;

    /**
	 * This method retrieves all the business units for the given iam organization.
	 *
	 * @param hspIamOrgID, the iam organization
	 * @return List of business units.
	 */
    @Override
    public BusinessUnit getBusinessUnitByHSPIamOrgID(UUID hspIamOrgID) {
        Optional<BusinessUnitEntity> businessUnitEntity = businessUnitRepository.findByBusinessUnitExtSystemEntity_HspIamOrgId(hspIamOrgID);
        if(!businessUnitEntity.isPresent()) {
            return null;
        }
        return businessUnitMapper.entityToDto(businessUnitEntity.get());
    }

    /**
	 * This method retrieves all the business units for the given name.
	 *
	 * @param name, the business unit name
	 * @return List of business units.
	 */
    @Override
    public BusinessUnit getBusinessUnitByName(String name) {
        Optional<BusinessUnitEntity> businessUnitEntity = businessUnitRepository.findByName(name);
        return businessUnitEntity.map(unitEntity -> businessUnitMapper.entityToDto(unitEntity)).orElse(null);
    }

    /**
	 * This method retrieves all the business units.
	 *
	 * @return List of business units.
	 */
    @Override
    public List<BusinessUnit> getBusinessCategories(){
        List<BusinessUnitEntity> businessUnitEntities = businessUnitRepository
                .findAll(Sort.by("cluster", "businessSegments"));
       return businessUnitMapper.mapBusinessCluster(businessUnitEntities);
    }
}
