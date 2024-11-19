/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: EmailNotificationType.java
 */

package com.philips.onespace.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * This enum captures the different types of notifications that can be sent via email.
 * The enum value should match the template name in SES.
 * For example, when an app is registered, an email notification is sent to the user
 * based on the template named "app-registered".
 */
public enum EmailNotificationType {
    APP_REGISTERED("app-registered"),
    APP_BUSINESS_OWNER_APPROVED("app-business-owner-approved"),
    APP_SOLUTION_OWNER_APPROVED("app-solution-owner-approved");

    private String value;

    EmailNotificationType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static EmailNotificationType fromValue(String text) {
        for (EmailNotificationType b : EmailNotificationType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
