/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: Customer.java
 */

package com.philips.onespace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;


@Validated
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Customer {


  @JsonProperty("id")
  private UUID id;

  @NotNull
  @JsonProperty("name")
  @NotEmpty
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("hspIamOrgId")
  private UUID hspIamOrgId;

  @JsonProperty("sapId")
  @Size(min=3, max = 255, message= "size_limit")
  private String sapId;

  @JsonProperty("market")
  @Valid
  private Market market;
}
