/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: PropositionMapper.java
 */

package com.philips.onespace.appdiscoveryframework.mapper;

import org.mapstruct.Mapper;

import com.philips.onespace.dto.Proposition;
import com.philips.onespace.jpa.entity.PropositionEntity;

@Mapper(componentModel = "spring")
public interface PropositionMapper {

    Proposition entityToDto(PropositionEntity propositionEntity);

    PropositionEntity dtoToEntity(Proposition proposition);
}
