/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: RoleEnum.java
 */

package com.philips.onespace.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleEnum {
    PLATFORMADMINISTRATORROLE("PLATFORMADMINISTRATORROLE"),
    APPLICATIONINTEGRATORROLE("APPLICATIONINTEGRATORROLE"),
    APPLICATIONOWNERROLE("APPLICATIONOWNERROLE"),
    HOSPITALENDUSERROLE("HOSPITALENDUSERROLE"),
    HOSPITALSITEADMINISTRATORROLE("HOSPITALSITEADMINISTRATORROLE"),
    KEYACCOUNTMANAGERROLE("KEYACCOUNTMANAGERROLE"),
    SOLUTIONAPPROVERROLE("SOLUTIONAPPROVERROLE");

    private String value;

    RoleEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RoleEnum fromValue(String text) {
        for (RoleEnum b : RoleEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
