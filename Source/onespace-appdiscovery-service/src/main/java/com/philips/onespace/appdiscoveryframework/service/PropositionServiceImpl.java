/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: PropositionServiceImpl.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.service.interfaces.PropositionService;
import com.philips.onespace.dto.Proposition;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.PropositionEntity;
import com.philips.onespace.jpa.repository.PropositionRepository;
import com.philips.onespace.util.ErrorMessages;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class PropositionServiceImpl implements PropositionService {

    @Autowired
    private PropositionRepository propositionRepository;

    /**
   	 * This method retrieves all the propositions.
   	 *
   	 * @return List of propositions.
   	 */
    @Override
    @Transactional
    public List<Proposition> listPropositions() throws ResourceNotFoundException, InvalidTokenException {
        return listAllPropositions();
    }

    private List<Proposition> listAllPropositions() throws ResourceNotFoundException{
        List<PropositionEntity> propositionEntities;
        try {
            propositionEntities = propositionRepository.findAll();
            if(null == propositionEntities || propositionEntities.size() == 0) {
            	throw new ResourceNotFoundException(ErrorMessages.NO_PROPOSITIONS_FOUND);
            }
        } catch (Exception expObj) {
            throw expObj;
        }

        List<Proposition> propositions = new ArrayList<>(propositionEntities.size());
        for (PropositionEntity entity : propositionEntities) {
            Proposition proposition = new Proposition();
            // Set the properties of the Proposition object using the 
            // corresponding properties of the PropositionEntity object

            proposition.setId(entity.getId());
            proposition.setName(entity.getName());
            proposition.setDescription(entity.getDescription());
            
            propositions.add(proposition);
        }
        return propositions;
    }
}