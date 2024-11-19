/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ApplicationStatusService.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.mapper.ValueMapper;
import com.philips.onespace.dto.Value;
import com.philips.onespace.jpa.entity.ApplicationStatusEntity;
import com.philips.onespace.jpa.repository.ApplicationStatusRepository;

@Service
public class ApplicationStatusService {
    @Autowired
    private ValueMapper valueMapper;

    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;

    /**
	 * This method retrieves all the app status.
	 *
	 * @return List of ApplicationStatus
	 */
    public List<Value> getApplicationStatus(){
        List<ApplicationStatusEntity> statusEntities = applicationStatusRepository.findAll();
        return valueMapper.mapApplicationStatus(statusEntities);
    }

}
