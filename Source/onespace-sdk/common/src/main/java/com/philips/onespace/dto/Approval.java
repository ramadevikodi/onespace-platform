/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: Approval.java
 */

package com.philips.onespace.dto;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.philips.onespace.util.Constants;
import com.philips.onespace.validator.ValidLocalDateTime;

import jakarta.validation.Valid;
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
public class Approval   {


  @JsonProperty("approver")
  @Size(min=3, max = 255, message= "size_limit")
  private String approver;

  @Valid
  @JsonProperty("publishedDateTime")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @ValidLocalDateTime(message = "date_past_expiry")
  @JsonFormat(pattern = Constants.DATE_FORMAT_UTC)
  private ZonedDateTime publishedDateTime;


  @JsonProperty("l2comments")
  @Size(min=3, max = 512, message= "size_limit")
  private String l2comments;

  @JsonProperty("propositions")
  @Valid
  private List<Proposition> propositions;

  @JsonProperty("markets")
  @Valid
  private List<Market> markets;
}
