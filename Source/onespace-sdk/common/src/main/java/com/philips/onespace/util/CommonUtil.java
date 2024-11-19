/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: CommonUtil.java
 */

package com.philips.onespace.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;

public class CommonUtil {

	public static boolean isPatternValidated(String providedVersion, String supportedVersion) {
		return Pattern.matches(supportedVersion, providedVersion);
	}
	
	public static void validateVersion(final String serviceVersion, final String requestedVersion) throws BadRequestException {
        if (!StringUtils.isBlank(requestedVersion) && requestedVersion.matches("[0-9]+")) {
            final int majorApiVersion = Integer.parseInt(serviceVersion);
            final int majorVersion = Integer.parseInt(requestedVersion);
            if (majorApiVersion != majorVersion) {
                throw new BadRequestException(ErrorMessages.INVALID_API_VERSION);
            }
        }else{
        	throw new BadRequestException(ErrorMessages.INVALID_API_VERSION);
        }
    }
	
}
