/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: EmailNotification.java
 */

package com.philips.onespace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Validated
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EmailNotification {

	@NotEmpty
	@NotNull
	private List<@Email(message = "invalid_email_to_address") String> to;

	private List<@Email String> cc;

	@NotBlank(message = "mandatory_email_subject")
	private String subject;

	@NotBlank(message = "mandatory_email_message")
	private String message;

	/**
	 * The type of email notification to be sent.
	 * For example: app-registered
	 */
	private EmailNotificationType type;

	/**
	 * The metadata that will be used to populate the placeholders in email template.
	 */
	private Map<String, String> metadata;

	// TODO :: Add support for attachments & category
}
