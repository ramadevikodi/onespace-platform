/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: UserMarketMappingService.java
 */

package com.philips.onespace.appdiscoveryframework.service.interfaces;

import java.util.List;
import java.util.UUID;

import com.philips.onespace.dto.UserMarketMap;

public interface UserMarketMappingService {

	 /**
   	 * This method retrieves all the languages.
   	 *
   	 * @param hspUserUuid, the user id
   	 * @return List of languages.
   	 */
    List<UserMarketMap> getMarketsAssociatedToUser(UUID hspUserUuid);

}
