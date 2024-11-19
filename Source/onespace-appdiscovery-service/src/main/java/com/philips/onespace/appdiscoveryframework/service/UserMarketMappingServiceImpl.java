/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: UserMarketMappingServiceImpl.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.mapper.UserMarketMapMapper;
import com.philips.onespace.appdiscoveryframework.service.interfaces.UserMarketMappingService;
import com.philips.onespace.dto.UserMarketMap;
import com.philips.onespace.jpa.entity.UserMarketMapEntity;
import com.philips.onespace.jpa.repository.UserMarketMappingRepository;

@Service
public class UserMarketMappingServiceImpl implements UserMarketMappingService {

    @Autowired
    private UserMarketMappingRepository userMarketMappingRepository;

    @Autowired
    private UserMarketMapMapper userMarketMapMapper;

    /**
   	 * This method retrieves all the markets associated with user.
   	 *
   	 * @param hspUserUuid, the user id
   	 * @return List of languages.
   	 */
    @Override
    public List<UserMarketMap> getMarketsAssociatedToUser(UUID hspUserUuid) {
        List<UserMarketMapEntity> userMarketMapEntityList = userMarketMappingRepository.findAllByHspUserUuid(hspUserUuid);
        return userMarketMapMapper.map(userMarketMapEntityList);
    }
}
