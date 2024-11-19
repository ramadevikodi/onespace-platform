/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ModalityService.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.mapper.ValueMapper;
import com.philips.onespace.dto.Value;
import com.philips.onespace.jpa.entity.ModalityEntity;
import com.philips.onespace.jpa.repository.ModalityRepository;

@Service
public class ModalityService {

    @Autowired
    private ModalityRepository modalityRepository;

    @Autowired
    private ValueMapper valueMapper;

    /**
   	 * This method retrieves all the modalities.
   	 *
   	 * @return List of modalities.
   	 */
    public List<Value> getModalities(){
        List<ModalityEntity> modalityEntities = modalityRepository.findAll();
        return valueMapper.mapModalities(modalityEntities);
    }

}
