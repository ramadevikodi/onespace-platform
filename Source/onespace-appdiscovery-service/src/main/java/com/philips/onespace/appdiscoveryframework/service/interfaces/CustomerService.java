/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: CustomerService.java
 */

package com.philips.onespace.appdiscoveryframework.service.interfaces;

import java.util.UUID;

import com.philips.onespace.dto.Customer;
import com.philips.onespace.exception.ResourceNotFoundException;

public interface CustomerService {

	/**
	 * This method retrieves all the Customers for the given iam organization.
	 *
	 * @param hspIamOrgID, the iam organization
	 * @return List of customers.
	 */
    Customer getCustomerByHSPIamOrgID(UUID hspIamOrgID) throws ResourceNotFoundException;

}
