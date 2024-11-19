/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NonOneSpaceApplicationMapper.java
 */

package com.philips.onespace.appdiscoveryframework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.philips.onespace.dto.NonOneSpaceApplication;
import com.philips.onespace.jpa.entity.NonOneSpaceApplicationEntity;

@Mapper(componentModel = "spring")
public interface NonOneSpaceApplicationMapper {

	NonOneSpaceApplication entityToDto(NonOneSpaceApplicationEntity applicationEntity);

	@Mapping(source = "order", target = "order", defaultValue = "99999999")
	@Mapping(target = "registeredBy", ignore = true)
	@Mapping(target = "registeredDateTime", ignore = true)
	@Mapping(source = "status", target = "status", defaultValue = "active")
	NonOneSpaceApplicationEntity dtoToEntity(NonOneSpaceApplication application);

}
