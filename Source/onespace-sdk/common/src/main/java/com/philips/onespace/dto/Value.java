/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: Value.java
 */

package com.philips.onespace.dto;


import java.util.UUID;

import org.springframework.validation.annotation.Validated;

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
public class Value {


    @Valid
    private UUID id;

    @Size(max = 256, message= "size_limit")
    private String name;

    @Valid
    private String description;

}
