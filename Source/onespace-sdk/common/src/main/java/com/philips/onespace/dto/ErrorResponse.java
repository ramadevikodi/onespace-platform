/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ErrorResponse.java
 */

package com.philips.onespace.dto;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Validated
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ErrorResponse {

	private List<Issue> issue;

	@Validated
	@Data
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	@EqualsAndHashCode
	public static class Issue {
		@JsonProperty("message")
		@Valid
		private String message;

		@JsonProperty("code")
		@Valid
		private String code;

		@JsonProperty("source")
		@Valid
		private String source;

		@JsonProperty("category")
		@Valid
		private String category;

		@JsonProperty("timestamp")
		private String timestamp;
	}
}
