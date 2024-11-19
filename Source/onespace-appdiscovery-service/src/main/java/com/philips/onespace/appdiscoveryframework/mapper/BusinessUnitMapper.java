/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: BusinessUnitMapper.java
 */

package com.philips.onespace.appdiscoveryframework.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.philips.onespace.dto.BusinessUnit;
import com.philips.onespace.jpa.entity.BusinessUnitEntity;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusinessUnitMapper {

    @Mapping(source = "businessUnitExtSystemEntity.hspIamOrgId", target = "hspIamOrgId")
    BusinessUnit entityToDto(BusinessUnitEntity businessUnitEntity);

    @Mapping(source = "hspIamOrgId", target = "businessUnitExtSystemEntity.hspIamOrgId")
    BusinessUnitEntity dtoToEntity(BusinessUnit businessUnit);

    List<BusinessUnit> mapBusinessCluster(List<BusinessUnitEntity> businessUnitEntities);

}
