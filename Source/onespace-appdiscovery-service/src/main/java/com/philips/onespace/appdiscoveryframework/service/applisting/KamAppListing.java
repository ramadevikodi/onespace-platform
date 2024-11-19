/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: KamAppListing.java
 */

package com.philips.onespace.appdiscoveryframework.service.applisting;

import static com.philips.onespace.logging.LoggingAspect.logData;
import static com.philips.onespace.util.IamConstants.HSP_IAM_SELF_SERVICE_APP_NAME;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.philips.onespace.appdiscoveryframework.service.interfaces.AppRegistrationService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.RoleBasedAppListing;
import com.philips.onespace.appdiscoveryframework.service.interfaces.UserMarketMappingService;
import com.philips.onespace.dto.UserMarketMap;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.jpa.specification.ApplicationSpecifications;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.util.Constants;

@Component
public class KamAppListing implements RoleBasedAppListing {
    
	@Autowired
    private ApplicationSpecifications applicationSpecifications;
    
	@Autowired
    private UserMarketMappingService userMarketMappingService;
    
	@Autowired
    private AppRegistrationService appRegistrationService;
    
	@Autowired
    private ApplicationRepository applicationRepository;

    /**
     * List applications
     * @param introspectionResponse introspectionResponse
     * @param statusId statusId
     * @param criteria criteria
     * @param pageable pageable
     * @return Page<ApplicationEntity>
     * @throws InvalidTokenException InvalidTokenException
     */
    @Override
    public Page<ApplicationEntity> listApplications(IntrospectionResponse introspectionResponse, UUID statusId, Map<String, String> criteria, Pageable pageable) throws InvalidTokenException {
        Specification specification = applicationSpecifications.
                buildSpecification(criteria);
        return listApplicationsOfKAM(introspectionResponse, specification,
                pageable);
    }

    /**
     * List applications of KAM
     * @param introspectionResponse introspectionResponse
     * @param specification specification
     * @param pageable pageable
     * @return Page<ApplicationEntity>
     */
    public Page<ApplicationEntity> listApplicationsOfKAM(IntrospectionResponse introspectionResponse, Specification specification, Pageable pageable) {
        List<ApplicationEntity> applicationEntities;
        // 1. Get admin apps for KAM - Sentinel, HSP IAM Self service
        applicationEntities = appRegistrationService.getAllApplicationsByName(List.of(HSP_IAM_SELF_SERVICE_APP_NAME,
                Constants.SENTINEL_APP_NAME, Constants.USER_MANAGEMENT_APP_NAME));
        // 2. Get markets associated with KAM user
        List<UserMarketMap> userMarketMapEntityList = userMarketMappingService.getMarketsAssociatedToUser(UUID.fromString(introspectionResponse.getSub()));
        logData("Getting the list of applications for KAM, admin-app-for-kam=",applicationEntities," markets-associated-with-kam=",userMarketMapEntityList);
        // 3. Get apps rolled out to the markets associated with KAM user
        if(!userMarketMapEntityList.isEmpty()) {
            List<ApplicationEntity> applicationRolledOutToMarket = applicationEntities;
            userMarketMapEntityList.forEach(userMarketMapEntity ->  {
                applicationRolledOutToMarket.addAll(appRegistrationService.getAllApplicationsPublishedToMarket(userMarketMapEntity.getMarketId()));
            });
        }
        logData(" application-list=",applicationEntities);
        if (applicationEntities.isEmpty()) {
            return Page.empty();
        }
        // Apply the specification and pagination to the filtered results
        Specification<ApplicationEntity> finalSpec = applicationSpecifications
                .getApplicationEntitySpecification(specification, applicationEntities);
        return applicationRepository.findAll(finalSpec, pageable);
    }

}
