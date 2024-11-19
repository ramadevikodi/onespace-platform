/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: UserMarketMapMapper.java
 */

package com.philips.onespace.appdiscoveryframework.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.philips.onespace.dto.UserMarketMap;
import com.philips.onespace.jpa.entity.UserMarketMapEntity;

@Mapper(componentModel = "spring")
public interface UserMarketMapMapper {

    UserMarketMap entityToDto(UserMarketMapEntity userMarketMapEntity);

    UserMarketMapEntity dtoToEntity(UserMarketMap userMarketMap);

    List<UserMarketMap> map(List<UserMarketMapEntity> userMarketMapEntityList);
}
