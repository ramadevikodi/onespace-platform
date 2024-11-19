/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: MarketService.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.mapper.MarketMapper;
import com.philips.onespace.dto.Market;
import com.philips.onespace.jpa.entity.MarketEntity;
import com.philips.onespace.jpa.repository.MarketRepository;

@Service
public class MarketService {

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private MarketMapper marketMapper;

    /**
   	 * This method retrieves all the markets.
   	 *
   	 * @return List of markets.
   	 */
    public List<Market> getMarket(){
        List<MarketEntity> marketEntities = marketRepository.findAll();
        return marketMapper.mapMarkets(marketEntities);
    }

}
