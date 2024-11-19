/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AppListingFactory.java
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
import java.util.Map;

import org.springframework.stereotype.Component;

import com.philips.onespace.appdiscoveryframework.service.applisting.ApplicationIntegratorAppListing;
import com.philips.onespace.appdiscoveryframework.service.applisting.ApplicationOwnerAppListing;
import com.philips.onespace.appdiscoveryframework.service.applisting.HospitalEndUserAppListing;
import com.philips.onespace.appdiscoveryframework.service.applisting.HospitalSiteAdminAppListing;
import com.philips.onespace.appdiscoveryframework.service.applisting.KamAppListing;
import com.philips.onespace.appdiscoveryframework.service.applisting.PlatformAdminAppListing;
import com.philips.onespace.appdiscoveryframework.service.applisting.SolutionApproverAppListing;
import com.philips.onespace.appdiscoveryframework.service.interfaces.RoleBasedAppListing;
import com.philips.onespace.dto.RoleEnum;

@Component
public class AppListingFactory {

	private Map<RoleEnum, RoleBasedAppListing> roleNameVsAppListing;

	public AppListingFactory(PlatformAdminAppListing platformAdminAppListing,
			ApplicationIntegratorAppListing applicationIntegratorAppListing,
			ApplicationOwnerAppListing applicationOwnerAppListing,
			SolutionApproverAppListing solutionApproverAppListing, KamAppListing kamAppListing,
			HospitalSiteAdminAppListing hospitalSiteAdminAppListing,
			HospitalEndUserAppListing hospitalEndUserAppListing) {
		roleNameVsAppListing = new EnumMap<>(RoleEnum.class);
		roleNameVsAppListing.put(PLATFORMADMINISTRATORROLE, platformAdminAppListing);
		roleNameVsAppListing.put(APPLICATIONINTEGRATORROLE, applicationIntegratorAppListing);
		roleNameVsAppListing.put(APPLICATIONOWNERROLE, applicationOwnerAppListing);
		roleNameVsAppListing.put(SOLUTIONAPPROVERROLE, solutionApproverAppListing);
		roleNameVsAppListing.put(KEYACCOUNTMANAGERROLE, kamAppListing);
		roleNameVsAppListing.put(HOSPITALSITEADMINISTRATORROLE, hospitalSiteAdminAppListing);
		roleNameVsAppListing.put(HOSPITALENDUSERROLE, hospitalEndUserAppListing);
	}

	public RoleBasedAppListing getRoleBasedAppListing(RoleEnum role) {
		return roleNameVsAppListing.get(role);
	}

}
