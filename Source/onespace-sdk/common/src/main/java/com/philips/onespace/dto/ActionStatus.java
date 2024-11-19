package com.philips.onespace.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ActionStatus {
    TODO("todo"),
    DONE("done"),
    EXPIRED("expired");

    private final String value;

    ActionStatus(String value) {
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
