/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: DeploymentModeService.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.mapper.ValueMapper;
import com.philips.onespace.dto.Value;
import com.philips.onespace.jpa.entity.DeploymentEntity;
import com.philips.onespace.jpa.repository.DeploymentRepository;

@Service
public class DeploymentModeService {

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private ValueMapper valueMapper;

    /**
	 * This method retrieves all the deployment modes.
	 *
	 * @return List of deployment modes.
	 */
    public List<Value> getDeploymentMode(){
        List<DeploymentEntity> deploymentEntities =  deploymentRepository.findAll(Sort.by("Mode"));
        return valueMapper.mapDeploymentModes(deploymentEntities);
    }

}
