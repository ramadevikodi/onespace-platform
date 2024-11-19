/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: Registration.java
 */

package com.philips.onespace.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.philips.onespace.validator.ValidInput;

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
public class Registration {


    @JsonProperty("name")
    @ValidInput(message = "invalid_application_name")
    @Size(max = 256, message= "size_limit")
    private String name;
    
    @JsonProperty("shortDescription")
    @ValidInput(message = "invalid_short_description", relaxedField = "invalid_short_description")
    @Size(max = 512, message= "size_limit")
    private String shortDescription;

    @JsonProperty("longDescription")
    @ValidInput(message = "invalid_long_description", relaxedField = "invalid_long_description")
    @Size(max = 2048, message= "size_limit")
    private String longDescription;


    @JsonProperty("version")
    @ValidInput(message = "invalid_application_version")
    @Size(max = 50, message= "size_limit")
    private String version;


    @JsonProperty("specialities")
    @Valid
    private List<Value> specialities;


    @JsonProperty("modalities")
    @Valid
    private List<Value> modalities;


    @JsonProperty("languages")
    @Valid
    private List<Value> languages;


    @JsonProperty("registeredBy")
    @Size(max = 120, message= "size_limit")
    private String registeredBy;

    @Valid
    @JsonProperty("registeredDateTime")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registeredDateTime;

    @JsonProperty("ownerOrganization")
    private UUID ownerOrganization;

    @JsonProperty("category")
    private Value category;


    @Valid
    @JsonProperty("businessUnit")
    private BusinessUnit businessUnit;

}
