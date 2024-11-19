/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NonOneSpaceApplicationService.java
 */

package com.philips.onespace.appdiscoveryframework.service.interfaces;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import org.apache.coyote.BadRequestException;

import com.philips.onespace.dto.NonOneSpaceApplication;
import com.philips.onespace.dto.NonOneSpaceApplicationOrder;
import com.philips.onespace.exception.ResourceNotFoundException;

public interface NonOneSpaceApplicationService {

	NonOneSpaceApplication save(NonOneSpaceApplication application, String maxlimit) throws BadRequestException, ParseException;

	List<NonOneSpaceApplication> getAll();

	NonOneSpaceApplication getById(UUID applicationId) throws ResourceNotFoundException;

	void updateById(UUID applicationId, NonOneSpaceApplication application) throws BadRequestException, ResourceNotFoundException;

	void updateAll(List<NonOneSpaceApplicationOrder> applicationOrders);

	void delete(UUID applicationId) throws ResourceNotFoundException;

}
