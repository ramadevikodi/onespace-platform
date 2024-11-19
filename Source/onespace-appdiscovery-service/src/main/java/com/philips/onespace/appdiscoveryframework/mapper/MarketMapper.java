/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: MarketMapper.java
 */

package com.philips.onespace.appdiscoveryframework.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.philips.onespace.dto.Market;
import com.philips.onespace.jpa.entity.MarketEntity;

@Mapper(componentModel = "spring")
public interface MarketMapper {

    Market entityToDto(MarketEntity marketEntity);

    MarketEntity dtoToEntity(Market market);

    List<Market> mapMarkets(List<MarketEntity> marketEntities);
}
