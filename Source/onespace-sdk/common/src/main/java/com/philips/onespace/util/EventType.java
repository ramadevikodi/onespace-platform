/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: EventType.java
 */

package com.philips.onespace.util;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EventType {
	DRAFT("Draft"), SUBMIT_FOR_APPROVAL("Submit for Approval"),
	AWAITING_BUSINESS_OWNER_APPROVAL("Awaiting Business Owner Approval"),
	APPROVED_BY_BUSINESS_OWNER("Approved by Business Owner"), REJECTED_BY_BUSINESS_OWNER("Rejected by Business Owner"),
	ON_HOLD_BY_BUSINESS_OWNER("On Hold by Business Owner"),
	AWAITING_SOLUTION_OWNER_APPROVAL("Awaiting Market/Solution Owner Approval"),
	APPROVED_BY_SOLUTION_OWNER("Approved by Market/Solution Owner"),
	ON_HOLD_SOUTION_OWNER("On Hold by Market/Solution Owner"), AWAITING_REQUESTER_ACTIOPN("Awaiting Requester action"),
	WITHDRAWN("Withdrawn"), APPROVED("Approved"), CANCELLED("Canceled"), PUBLISHED("Published"),
	ACTION_INITIATED("Action Initiated");

	private String eventType;

	private EventType(String eventType) {
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return this.eventType;
	}

	@JsonCreator
	public static EventType fromValue(String text) {
		for (EventType b : EventType.values()) {
			if (String.valueOf(b.eventType).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
