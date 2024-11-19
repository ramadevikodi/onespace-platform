/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: PropositionService.java
 */

package com.philips.onespace.appdiscoveryframework.service.interfaces;

import java.util.List;

import com.philips.onespace.dto.Proposition;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceNotFoundException;

public interface PropositionService {

    List<Proposition> listPropositions() throws ResourceNotFoundException, InvalidTokenException;

}