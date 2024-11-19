/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: Ownership.java
 */

package com.philips.onespace.dto;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.philips.onespace.validator.ValidInput;

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
public class Ownership {


    @JsonProperty("costCenter")
    @ValidInput(message = "invalid_cost_center")
    @Size(max = 50, message= "size_limit")
    private String costCenter;

    @JsonProperty("l1comments")
    @Size(min = 3, max = 512, message= "size_limit")
    private String l1comments;

}
