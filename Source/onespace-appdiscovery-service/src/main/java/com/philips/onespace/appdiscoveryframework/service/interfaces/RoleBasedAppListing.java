/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: RoleBasedAppListing.java
 */

package com.philips.onespace.appdiscoveryframework.service.interfaces;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.model.IntrospectionResponse;

public interface RoleBasedAppListing {

    public Page<ApplicationEntity> listApplications(IntrospectionResponse introspectionResponse,
                                                    UUID statusId,
                                                    Map<String, String> criteria,
                                                    Pageable pageable) throws InvalidTokenException, ResourceNotFoundException;
}
