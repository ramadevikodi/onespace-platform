/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: SolutionApproverAppListing.java
 */

package com.philips.onespace.appdiscoveryframework.service.applisting;

import static com.philips.onespace.logging.LoggingAspect.logData;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.philips.onespace.appdiscoveryframework.service.interfaces.AppRegistrationService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.BusinessUnitService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.RoleBasedAppListing;
import com.philips.onespace.appdiscoveryframework.util.IAMUtil;
import com.philips.onespace.dto.BusinessUnit;
import com.philips.onespace.dto.RoleEnum;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.jpa.specification.ApplicationSpecifications;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.util.Constants;

@Component
public class SolutionApproverAppListing implements RoleBasedAppListing {
    
	@Autowired
    private IAMUtil iamUtil;
    
	@Autowired
    private ApplicationSpecifications applicationSpecifications;
    
	@Autowired
    private BusinessUnitService businessUnitService;
    
	@Autowired
    private ApplicationRepository applicationRepository;
    
	@Autowired
    private AppRegistrationService appRegistrationService;

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
    public Page<ApplicationEntity> listApplications(IntrospectionResponse introspectionResponse,
                                                    UUID statusId,
                                                    Map<String, String> criteria,
                                                    Pageable pageable) throws InvalidTokenException {
        Specification specification = applicationSpecifications.buildSpecification(criteria);
        Page<ApplicationEntity> applicationEntities = listApplicationsOfBU(introspectionResponse,
                String.valueOf(RoleEnum.SOLUTIONAPPROVERROLE),
                specification,
                statusId,
                pageable);
        List<ApplicationEntity> filteredApplicationsList = applicationEntities.stream().filter(application -> !application.getStatus().getName().equals(Constants.DRAFT) &&
                !application.getStatus().getName().equals(Constants.AWAITING_BUSINESS_OWNER_APPROVAL) &&
                !application.getStatus().getName().equals(Constants.REJECTED_BY_BUSINESS_OWNER) &&
                !application.getStatus().getName().equals(Constants.ON_HOLD_BY_BUSINESS_OWNER)).collect(Collectors.toList());
        return new PageImpl<>(filteredApplicationsList, pageable, filteredApplicationsList.size());
    }
    
    /**
     * List applications of BU
     * @param introspectionResponse introspectionResponse
     * @param roleName roleName
     * @param specification specification
     * @param status status
     * @param pageable pageable
     * @return Page<ApplicationEntity>
     */
    public Page<ApplicationEntity> listApplicationsOfBU(IntrospectionResponse introspectionResponse, String roleName,
                                                        Specification specification,
                                                        UUID status,
                                                        Pageable pageable) {
        List<ApplicationEntity> applicationEntities = null;
        // 1. Introspect the token & get the BU User's HSP IAM Org ID
        UUID hspIamBuOrgID = UUID.fromString(iamUtil.findUserOrg(introspectionResponse, roleName));
        // 2. Get the BU ID associated with HSP IAM BU Org ID
        BusinessUnit businessUnit = businessUnitService.getBusinessUnitByHSPIamOrgID(hspIamBuOrgID);
        if(businessUnit == null) {
            return Page.empty();
        }
        // 3. Get application associated with BU ID = Owner Org ID and in Approved state
        applicationEntities = appRegistrationService.getAllApplicationsOfOwnerOrg(businessUnit.getId(),
                status);
        logData(" Getting the list of applications of BU, hsp-iam-bu-org-id=",hspIamBuOrgID," business-unit=",businessUnit,"application-list="+applicationEntities);
        if (applicationEntities.isEmpty()) {
            return Page.empty();
        }
        // Apply the specification and pagination to the filtered results
        Specification<ApplicationEntity> finalSpec = applicationSpecifications
                .getApplicationEntitySpecification(specification, applicationEntities);
        return applicationRepository.findAll(finalSpec, pageable);
    }
}
