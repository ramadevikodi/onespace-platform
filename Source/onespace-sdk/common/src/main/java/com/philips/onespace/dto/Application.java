/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: Application.java
 */

package com.philips.onespace.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.philips.onespace.model.Group;
import com.philips.onespace.validator.NotNull;

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
@NotNull
public class Application {

  @JsonProperty("id")
  private UUID id;


  @Valid
  @JsonProperty("registration")
  private Registration registration;

  @Valid
  @JsonProperty("deployment")
  private Deployment deployment;

  @Valid
  @JsonProperty("ownership")
  private Ownership ownership;

  @Valid
  @JsonProperty("approval")
  private Approval approval;

  private Value status;

  private boolean enabledForOrg;

  private boolean enabledForUser;

  @Valid
  @JsonProperty("lastModifiedDateTime")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime lastModifiedDateTime;

  private boolean isMFE;
  private boolean ssoEnabled;
  private boolean promoteAsUpComingApp;
  private boolean promoteAsNewApp;
  
  @JsonProperty("workflowParticipants")
  private List<Group> groups;
  
  private boolean isThirdParty;
}
