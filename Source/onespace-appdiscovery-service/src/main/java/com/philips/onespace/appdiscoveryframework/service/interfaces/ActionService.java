/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ActionService.java
 */

package com.philips.onespace.appdiscoveryframework.service.interfaces;

import java.util.List;

import org.apache.coyote.BadRequestException;

import com.philips.onespace.dto.Action;
import com.philips.onespace.dto.ActionId;

public interface ActionService {

	/**
	 * This method retrieves all the actions.
	 *
	 * @return List of actions
	 */
    List<Action> getActions();

    /**
	 * This method saves action.
	 *
	 * @param action, the action
	 * @return ActionId, the action id
	 */
    ActionId createAction(Action action) throws BadRequestException;

    /**
	 * This method updates action.
	 *
	 * @param action the action
	 * @return update action status
	 */
    int updateStatus(List<Action> action) throws BadRequestException;

    /**
	 * This method marks the expired actions.
	 *
	 * @return expired action status
	 */
    int markExpiredActions();
}
