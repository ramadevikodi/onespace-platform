/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AppListingService.java
 */

package com.philips.onespace.appdiscoveryframework.service.interfaces;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;

public interface AppListingService {

    Page<ApplicationEntity> listApplications(UUID status, String token,
                                             Map<String, String> criteria,
                                             Pageable pageable) throws ResourceNotFoundException, InvalidTokenException;

}
