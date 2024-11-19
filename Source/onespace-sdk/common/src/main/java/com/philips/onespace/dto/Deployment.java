/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: Deployment.java
 */

package com.philips.onespace.dto;

import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Deployment
 */
@Validated
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Deployment   {


  @JsonProperty("url")
  @URL(message = "invalid_url")
  @Size(max = 256, message= "size_limit")
  private String url;

  @JsonProperty("icon")
  @Size(max = 256, message= "size_limit")
  private String icon;

  @JsonProperty("mode")
  private Value mode;

  @JsonProperty("banner")
  @Size(max = 256, message= "size_limit")
  private String banner;

}
