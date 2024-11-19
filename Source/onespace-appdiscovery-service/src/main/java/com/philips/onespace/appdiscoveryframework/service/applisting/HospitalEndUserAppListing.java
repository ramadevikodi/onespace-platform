/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: HospitalEndUserAppListing.java
 */

package com.philips.onespace.appdiscoveryframework.service.applisting;

import static com.philips.onespace.logging.LoggingAspect.logData;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.philips.onespace.appdiscoveryframework.service.interfaces.AppRegistrationService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.CustomerService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.RoleBasedAppListing;
import com.philips.onespace.appdiscoveryframework.util.IAMUtil;
import com.philips.onespace.appdiscoveryframework.util.SecurityContextUtil;
import com.philips.onespace.dto.Customer;
import com.philips.onespace.dto.RoleEnum;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.jpa.specification.ApplicationSpecifications;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.sentinel.dto.OneSpaceProduct;
import com.philips.onespace.sentinel.service.LicenseManager;

@Component
public class HospitalEndUserAppListing implements RoleBasedAppListing {
    
	@Autowired
    private IAMUtil iamUtil;
    
	@Autowired
    private ApplicationSpecifications applicationSpecifications;
    
	@Autowired
    private CustomerService customerService;
    
	@Autowired
    private AppRegistrationService appRegistrationService;
    
	@Autowired
    private ApplicationRepository applicationRepository;
    
	@Autowired
    private LicenseManager licenseManager;
    
    @Autowired
	private SecurityContextUtil contextUtil;

    @Override
    public Page<ApplicationEntity> listApplications(IntrospectionResponse introspectionResponse, UUID statusId, Map<String, String> criteria, Pageable pageable) throws InvalidTokenException, ResourceNotFoundException {
        Specification specification = applicationSpecifications.
                buildSpecification(criteria);
        return listApplicationsOfHospital(introspectionResponse,
                specification,
                String.valueOf(RoleEnum.HOSPITALENDUSERROLE),
                pageable);
    }
    
    /**
     * List applications of Hospital
     * @param introspectionResponse introspectionResponse
     * @param specification specification
     * @param roleName roleName
     * @param pageable pageable
     * @return Page<ApplicationEntity>
     * @throws ResourceNotFoundException ResourceNotFoundException
     */
    public Page<ApplicationEntity> listApplicationsOfHospital(IntrospectionResponse introspectionResponse, Specification specification, String roleName, Pageable pageable) throws ResourceNotFoundException {
        List<ApplicationEntity> applicationEntities;
        // 1. Introspect the token & get the HospitalEndUser's HSP IAM Org ID
        UUID hspIamCustomerOrgID = UUID.fromString(iamUtil.findUserOrg(introspectionResponse, roleName));
        logData("Getting the list of applications of Hospital");
        // 2. Get the customer details associated with HSP IAM Customer Org ID
        Customer customer = customerService.getCustomerByHSPIamOrgID(hspIamCustomerOrgID);
        UUID customerId = customer.getId();
        UUID marketId = customer.getMarket().getMarketId();
        // 3. Get applications published to the market of customer
        applicationEntities = appRegistrationService.getAllApplicationsPublishedToMarket(marketId);
        logData(" applications-published-to-market-="+applicationEntities);
        // 4. Get the application IDs entitled for the customer
        List<OneSpaceProduct> oneSpaceProducts = licenseManager.getEntitledProductsForCustomer(String.valueOf(customerId));
        logData(" applications-entitled-for-customer="+oneSpaceProducts);
        // Fetch any Third Party Applications
        applicationEntities.addAll(fetchThirdPartyApplications());
        // Apply the specification and pagination to the filtered results
        Specification<ApplicationEntity> finalSpec = applicationSpecifications
                .getApplicationEntitySpecification(specification, applicationEntities);
        Page<ApplicationEntity> applications = applicationRepository.findAll(finalSpec, pageable);
        // 5. Enabled entitled app IDs, Disable remaining app IDs
        List<ApplicationEntity> enabledApplicationsList = applications.stream()
                .map(a -> {
                    updateEnabledFlags(a, oneSpaceProducts);
                    return a;
                })
                .toList();
        if (enabledApplicationsList.isEmpty()) {
            return Page.empty();
        }
        return new PageImpl<>(enabledApplicationsList, pageable, enabledApplicationsList.size());
    }

    /**
     * Fetch third-party applications
     * @return List<ApplicationEntity>
     */
    private List<ApplicationEntity> fetchThirdPartyApplications() {
        return appRegistrationService.getThirdPartyApplications();
    }

    /**
     * Update enabled flags
     * @param applicationEntity applicationEntity
     * @param oneSpaceProducts oneSpaceProducts
     */
    private void updateEnabledFlags(ApplicationEntity applicationEntity, List<OneSpaceProduct> oneSpaceProducts) {
        Optional<OneSpaceProduct> product = oneSpaceProducts.stream()
                .filter(c -> c.getExternalId().equals(applicationEntity.getId()))
                .findFirst();
        if (product.isEmpty()) {
            applicationEntity.setEnabledForOrg(false);
            applicationEntity.setEnabledForUser(false);
        } else {
            if (product.get().isNamedProduct()) {
                String userName = contextUtil.getUserNameFromPrincipal(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                List<String> namedUsers = product.get().getNamedUsers();
                if (namedUsers != null && !namedUsers.contains(userName)) {
                    applicationEntity.setEnabledForUser(false);
                }
            } else {
                applicationEntity.setEnabledForUser(false);
            }
        }
    }
}
