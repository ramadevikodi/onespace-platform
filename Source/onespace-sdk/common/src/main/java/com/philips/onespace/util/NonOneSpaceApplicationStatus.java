/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NonOneSpaceApplicationStatus.java
 */

package com.philips.onespace.util;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NonOneSpaceApplicationStatus {

	ENABLED("Enabled"), DISABLED("Disabled");
	
	private final String value;

	NonOneSpaceApplicationStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    public String getValue() {
        return value;
    }
	
}
