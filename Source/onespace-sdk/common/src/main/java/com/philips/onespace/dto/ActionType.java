package com.philips.onespace.dto;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Type Enum represents the type of action.
 * - Informational: Action that provides general information or update.
 * - Reminder: Action that reminds the user about upcoming events, tasks, or deadlines.
 * - Alert: Actions that are urgent or require immediate action.
 * - Feedback: Action that request feedback from the user.
 */

public enum ActionType {
    INFORMATIONAL("Informational"),
    REMINDER("Reminder"),
    ALERT("Alert"),
    FEEDBACK("Feedback");

    private final String value;

    ActionType(String value) {
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
