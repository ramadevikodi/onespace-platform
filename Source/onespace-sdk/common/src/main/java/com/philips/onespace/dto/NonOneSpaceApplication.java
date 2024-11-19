/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NonOneSpaceApplication.java
 */

package com.philips.onespace.dto;

import java.util.UUID;

import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.philips.onespace.util.NonOneSpaceApplicationStatus;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NonOneSpaceApplication {

	private UUID id;

	@Size(min = 1, max = 256, message = "size_limit_nononespaceapp_name")
	@NotBlank(message = "missing_name_param")
	private String name;

	@Size(min = 1, max = 512, message = "size_limit_nononespaceapp_description")
	@NotBlank(message = "missing_description_param")
	private String description;

	@Size(min = 1, max = 256, message = "size_limit_nononespaceapp_url")
	@NotBlank(message = "missing_url_param")
	@URL(message = "invalid_url")
	private String url;

	@ValidEnum(enumClass = NonOneSpaceApplicationStatus.class, message = "nononespaceapp_invalid_status")
	private String status;

	private Integer order;

	@Size(max = 256, message = "size_limit_nononespaceapp_registeredBy")
	private String registeredBy;

	@ValidDateFormat
	private String registeredDateTime;

	@JsonProperty("icon")
	@Size(max = 256, message = "size_limit_nononespaceapp_icon")
	private String icon;

}
