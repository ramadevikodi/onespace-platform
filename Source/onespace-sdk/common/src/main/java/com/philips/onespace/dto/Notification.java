/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: Notification.java
 */

package com.philips.onespace.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.philips.onespace.util.Category;
import com.philips.onespace.validator.ValidDateFormat;
import com.philips.onespace.validator.ValidEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class Notification {

	private UUID id;

	@Size(min = 1, max = 256, message= "size_limit_notification_title")
	@NotBlank(message = "missing_notification_title_param")
	private String title;

	@NotBlank(message = "missing_notification_message_param")
	@Size(min = 1, max = 2048, message= "size_limit_notification_message")
	private String message;

	@ValidEnum(enumClass = Category.class, message = "invalid_notification_category")
	private String category;

	private Source source;

	@JsonProperty("createdAt")
	@NotBlank(message = "missing_notification_createdat_param")
	@ValidDateFormat
	private String createdAt;

	@JsonProperty("expiry")
	@ValidDateFormat
	private String expiry;
	
	private List<String> recipients;
	
	private Boolean read = Boolean.FALSE;

}
