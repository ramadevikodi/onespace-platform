/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: LicensingServiceImpl.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import static com.philips.onespace.dto.RoleEnum.HOSPITALENDUSERROLE;
import static com.philips.onespace.dto.RoleEnum.HOSPITALSITEADMINISTRATORROLE;
import static com.philips.onespace.logging.LoggingAspect.logData;
import static com.philips.onespace.util.Constants.LICENSES_LIMIT_CACHE_KEY;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.service.interfaces.CustomerService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.LicensingService;
import com.philips.onespace.appdiscoveryframework.util.IAMUtil;
import com.philips.onespace.cache.GuavaCacheUtil;
import com.philips.onespace.dto.SecurityContextDetail;
import com.philips.onespace.exception.EntitlementNotFoundException;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.LicenseConsumptionException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.sentinel.service.LicenseManager;
import com.philips.onespace.util.ErrorMessages;

@Service
public class LicensingServiceImpl implements LicensingService {

    @Autowired
    private GuavaCacheUtil<AtomicInteger> guavaCacheUtil;
    
    @Autowired
    private IAMUtil iamUtil;
    
    @Autowired
    private LicenseManager licenseManager;
    
    @Autowired
    private CustomerService customerService;
    
    @Override
    public void consumeLicense(String token, UUID appId) throws InvalidTokenException, ResourceNotFoundException, EntitlementNotFoundException, LicenseConsumptionException {
        logData(" token=",token);
        if(hasEntitlement(token, appId)) {
            String key = getCacheKey(token, appId);
            String userKey = getUserKey(key);
            logData(" key=",key," userKey=",userKey);
            if (hasUserConsumedLicense(userKey)) {
                throw new LicenseConsumptionException(ErrorMessages.LICENSE_USER_CONSUMED_ERR_CODE);
            }
            int currentLicenseCount = guavaCacheUtil.get(key).get();
            logData(" current-license-count=",currentLicenseCount);
            if (currentLicenseCount > 0) {
                guavaCacheUtil.get(key).decrementAndGet();
                guavaCacheUtil.add(userKey, new AtomicInteger(1));
            } else {
                throw new LicenseConsumptionException(ErrorMessages.LICENSE_LIMIT_EXCEEDED_ERR_CODE);
            }
        } else {
            throw new EntitlementNotFoundException(ErrorMessages.ENTITLEMENT_NOT_FOUND_ERR_CODE);
        }
    }

    @Override
    public void releaseLicense(String token, UUID appId) throws InvalidTokenException, ResourceNotFoundException, LicenseConsumptionException, EntitlementNotFoundException {
        logData(" token=",token);
        if(hasEntitlement(token, appId)) {
            String key = getCacheKey(token, appId);
            String userKey = getUserKey(key);
            logData(" key=",key," userKey=",userKey);
            if (!hasUserConsumedLicense(userKey)) {
                throw new LicenseConsumptionException(ErrorMessages.LICENSE_USER_NOT_CONSUMED_ERR_CODE);
            }
            if (guavaCacheUtil.get(key) != null) {
                int currentLicenseCount = guavaCacheUtil.get(key).get();
                logData(" current-license-count=",currentLicenseCount);
                if (currentLicenseCount < getLicenseLimit(key)) {
                    guavaCacheUtil.get(key).incrementAndGet();
                    guavaCacheUtil.invalidate(userKey);
                }
            }
        } else {
            throw new EntitlementNotFoundException(ErrorMessages.ENTITLEMENT_NOT_FOUND_ERR_CODE);
        }
    }

    public boolean hasEntitlement(String token, UUID appId) throws InvalidTokenException, ResourceNotFoundException {
        UUID customerId = getLoggedInCustomerId(token);
        if(customerId != null) {
            String entitlementId = licenseManager.getEntitlementIdForCustomerProduct(appId, customerId);
            logData(" Check entitlement for customer-id=" + customerId, " entitlement-id =" + entitlementId);
            if (entitlementId != null) {
                String key = getCacheKey(customerId, appId);
                if (guavaCacheUtil.get(key) == null) {
                    int licenseLimit = Integer.parseInt(licenseManager.getFeatureLicenseLimitOfEntitledProduct(entitlementId, String.valueOf(appId)));
                    guavaCacheUtil.add(key + LICENSES_LIMIT_CACHE_KEY, new AtomicInteger(licenseLimit));
                    guavaCacheUtil.add(key, new AtomicInteger(licenseLimit));
                }
                logData(" entitlement=true");
                return true;
            }
            logData(" entitlement=false");
        }
        return false;
    }

    private int getLicenseLimit(String key) {
        return guavaCacheUtil.get(key + LICENSES_LIMIT_CACHE_KEY).get();
    }

    private String getCacheKey(String token, UUID appId) throws InvalidTokenException, ResourceNotFoundException {
        return appId + "_" + getLoggedInCustomerId(token);
    }

    private String getCacheKey(UUID customerId, UUID appId) {
        return appId + "_" + customerId;
    }

    private UUID getLoggedInCustomerId(String token) throws InvalidTokenException, ResourceNotFoundException {
        String loggedInCustomerRole = getLoggedInCustomerRole(token);
        logData(" customer-role=" + loggedInCustomerRole);
        if (loggedInCustomerRole != null) {
            UUID hspIamCustomerOrgID = iamUtil.findUserOrg(token, loggedInCustomerRole);
            logData(" customer-org-id=" + hspIamCustomerOrgID);
            return customerService.getCustomerByHSPIamOrgID(hspIamCustomerOrgID).getId();
        }
        return null;
    }

    private String getLoggedInCustomerRole(String token) throws InvalidTokenException {
        if(iamUtil.hasRole(token, String.valueOf(HOSPITALENDUSERROLE))) {
            return String.valueOf(HOSPITALENDUSERROLE);
        } else if(iamUtil.hasRole(token, String.valueOf(HOSPITALSITEADMINISTRATORROLE))) {
            return String.valueOf(HOSPITALSITEADMINISTRATORROLE);
        }
        return null;
    }

    private boolean hasUserConsumedLicense(String userKey) {
        return guavaCacheUtil.get(userKey) != null;
    }

    private static String getUserKey(String key) {
    	SecurityContextDetail principalObject = (SecurityContextDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return key + "_" + principalObject.getUserName();
    }
}
