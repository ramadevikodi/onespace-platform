/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: FilterCriteriaFactory.java
 */
package com.philips.onespace.appdiscoveryframework.factory;

import static com.philips.onespace.dto.RoleEnum.APPLICATIONINTEGRATORROLE;
import static com.philips.onespace.dto.RoleEnum.APPLICATIONOWNERROLE;
import static com.philips.onespace.dto.RoleEnum.HOSPITALENDUSERROLE;
import static com.philips.onespace.dto.RoleEnum.HOSPITALSITEADMINISTRATORROLE;
import static com.philips.onespace.dto.RoleEnum.KEYACCOUNTMANAGERROLE;
import static com.philips.onespace.dto.RoleEnum.PLATFORMADMINISTRATORROLE;
import static com.philips.onespace.dto.RoleEnum.SOLUTIONAPPROVERROLE;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.philips.onespace.appdiscoveryframework.service.ApplicationIntegratorCriteria;
import com.philips.onespace.appdiscoveryframework.service.HospitalEndUserCriteria;
import com.philips.onespace.appdiscoveryframework.service.KamCriteria;
import com.philips.onespace.appdiscoveryframework.service.PlatformAdminCriteria;
import com.philips.onespace.appdiscoveryframework.service.interfaces.RoleBasedFilterCriteria;
import com.philips.onespace.dto.ApplicationFilter;
import com.philips.onespace.dto.RoleEnum;

@Component
public class FilterCriteriaFactory {
    
	private Map<RoleEnum,RoleBasedFilterCriteria> roleNameVsFilterCriteria;

    @Autowired
    public FilterCriteriaFactory(PlatformAdminCriteria platformAdminCriteria,
                                 ApplicationIntegratorCriteria applicationIntegratorCriteria,
                                 KamCriteria kamCriteria,
                                 HospitalEndUserCriteria hospitalEndUserCriteria) {
        roleNameVsFilterCriteria = new EnumMap<>(RoleEnum.class);
        roleNameVsFilterCriteria.put(PLATFORMADMINISTRATORROLE, platformAdminCriteria);
        roleNameVsFilterCriteria.put(APPLICATIONINTEGRATORROLE, applicationIntegratorCriteria);
        roleNameVsFilterCriteria.put(APPLICATIONOWNERROLE, applicationIntegratorCriteria);
        roleNameVsFilterCriteria.put(SOLUTIONAPPROVERROLE, applicationIntegratorCriteria);
        roleNameVsFilterCriteria.put(KEYACCOUNTMANAGERROLE, kamCriteria);
        roleNameVsFilterCriteria.put(HOSPITALSITEADMINISTRATORROLE, hospitalEndUserCriteria);
        roleNameVsFilterCriteria.put(HOSPITALENDUSERROLE, hospitalEndUserCriteria);
    }

    public List<ApplicationFilter> getRoleBasedFilterCriteria(RoleEnum role){
        RoleBasedFilterCriteria roleBasedFilterCriteria = roleNameVsFilterCriteria.get(role);
        return roleBasedFilterCriteria.getCriteria();
    }
}
