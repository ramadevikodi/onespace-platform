/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: UserResponse.java
 */

package com.philips.onespace.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@SuppressFBWarnings(value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class UserResponse {
	
	@JsonProperty("urn:ietf:params:scim:schemas:extension:philips:hsdp:2.0:Group")
	private UrnGroup urnGroup;
	
}
