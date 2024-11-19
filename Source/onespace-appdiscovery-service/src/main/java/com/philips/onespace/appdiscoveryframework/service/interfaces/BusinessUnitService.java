/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: BusinessUnitService.java
 */

package com.philips.onespace.appdiscoveryframework.service.interfaces;

import java.util.List;
import java.util.UUID;

import com.philips.onespace.dto.BusinessUnit;

public interface BusinessUnitService {

	/**
	 * This method retrieves all the business units for the given iam organization.
	 *
	 * @param hspIamOrgID, the iam organization
	 * @return List of business units.
	 */
    BusinessUnit getBusinessUnitByHSPIamOrgID(UUID hspIamOrgID);

    /**
	 * This method retrieves all the business units for the given name.
	 *
	 * @param name, the business unit name
	 * @return List of business units.
	 */
    BusinessUnit getBusinessUnitByName(String name);

    /**
	 * This method retrieves all the business units.
	 *
	 * @return List of business units.
	 */
    List<BusinessUnit> getBusinessCategories();

}
