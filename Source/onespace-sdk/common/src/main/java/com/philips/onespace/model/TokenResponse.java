/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: TokenResponse.java
 */

package com.philips.onespace.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TokenResponse {
    private String scope;

    private String token_type;

    private String access_token;

    private String refresh_token;

    private Long expires_in;


    @Override
    public String toString() {
        return "TokenResponse{" +
            "scope='" + scope + '\'' +
            ", token_type='" + token_type + '\'' +
            ", access_token='" + access_token.replaceAll("(?<=.{4}).", "*") + '\'' +
            ", refresh_token='" + (null != refresh_token ? refresh_token.replaceAll("(?<=.{4}).", "*") : null) + '\'' +
            ", expires_in=" + expires_in +
            '}';
    }
}
