/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: PlatformAdminAppListing.java
 */

package com.philips.onespace.appdiscoveryframework.service.applisting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.philips.onespace.appdiscoveryframework.service.interfaces.AppRegistrationService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.RoleBasedAppListing;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.specification.ApplicationSpecifications;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.util.Constants;

@Component
public class PlatformAdminAppListing implements RoleBasedAppListing {

    @Autowired
    private AppRegistrationService appRegistrationService;

    @Autowired
    private ApplicationSpecifications applicationSpecifications;

    /**
     * List applications
     * @param introspectionResponse introspectionResponse
     * @param statusId statusId
     * @param criteria criteria
     * @param pageable pageable
     * @return Page<ApplicationEntity>
     */
    @Override
    public Page<ApplicationEntity> listApplications(IntrospectionResponse introspectionResponse,
                                                    UUID statusId,
                                                    Map<String, String> criteria,
                                                    Pageable pageable) {
        Specification specification = applicationSpecifications.buildSpecification(criteria);
        Page<ApplicationEntity> applicationEntities = appRegistrationService
                .getAllApplications(statusId, specification, pageable);
        List<ApplicationEntity> applicationlist = new ArrayList<>(applicationEntities.getContent());
        applicationlist.add(appRegistrationService
                .getApplication(Constants.USER_MANAGEMENT_APP_NAME));
        return new PageImpl<>(applicationlist, applicationEntities.getPageable(),
                applicationEntities.getTotalElements() + 1);
    }
}
