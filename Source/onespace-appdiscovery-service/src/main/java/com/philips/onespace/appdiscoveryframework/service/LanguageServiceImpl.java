/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: LanguageServiceImpl.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.mapper.ValueMapper;
import com.philips.onespace.dto.Value;
import com.philips.onespace.jpa.entity.LanguageEntity;
import com.philips.onespace.jpa.repository.LanguageRepository;

@Service
public class LanguageServiceImpl {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ValueMapper valueMapper;

    /**
   	 * This method retrieves all the languages.
   	 *
   	 * @return List of languages.
   	 */
    public List<Value> getLanguages() {
        List<LanguageEntity> languageEntities = languageRepository.findAll();
        return valueMapper.mapLanguages(languageEntities);
    }
}
