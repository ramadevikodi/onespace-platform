/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AppRegistrationService.java
 */

package com.philips.onespace.appdiscoveryframework.service.interfaces;


import java.util.List;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.philips.onespace.dto.Application;
import com.philips.onespace.exception.DatabaseConstraintViolationException;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceExistsException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;

public interface AppRegistrationService {

    Application registerApplication(Application application)
            throws DatabaseConstraintViolationException, InvalidTokenException, ResourceExistsException, BadRequestException;

    Application updateApplication(UUID id, Application application)
            throws ResourceNotFoundException, DatabaseConstraintViolationException, ResourceExistsException, BadRequestException;

    ApplicationEntity getApplication(UUID id) throws ResourceNotFoundException;

    Page<ApplicationEntity> getAllApplications(UUID statusId, Specification specification, Pageable pageable);

    List<ApplicationEntity> getAllApplications(List<UUID> uuuids);

    List<ApplicationEntity> getAllApplicationsOfOwnerOrg(UUID ownerOrg, UUID statusId);

    ApplicationEntity getApplication(String name);

    List<ApplicationEntity> getAllApplicationsByName(List<String> names);

    List<ApplicationEntity> getAllApplicationsOfACategory(String categoryName);

    List<ApplicationEntity> getAllApplicationsPublishedToMarket(UUID marketId);

    List<ApplicationEntity> getThirdPartyApplications();
}
