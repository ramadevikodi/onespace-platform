/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: PlatformAdminCriteria.java
 */
package com.philips.onespace.appdiscoveryframework.service;

import com.philips.onespace.appdiscoveryframework.mapper.ApplicationMapper;
import com.philips.onespace.appdiscoveryframework.service.interfaces.RoleBasedFilterCriteria;
import com.philips.onespace.dto.ApplicationFilter;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlatformAdminCriteria implements RoleBasedFilterCriteria {
    @Autowired
    private ApplicationMapper applicationMapper;
    
    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public List<ApplicationFilter> getCriteria() {
        return applicationMapper
                .convertToApplicationFilter(applicationRepository
                        .getFilterCriteriaForPlatformAdmin());
    }
}
