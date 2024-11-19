/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: IntrospectionResponse.java
 */

package com.philips.onespace.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class IntrospectionResponse {

    private Boolean active;

    private String scope;

    private String clientId;

    private String username;

    private String tokenType;

    private Long exp;

    private String sub;

    private String iss;
    private IdentityTypeEnum identityType;
    private String deviceType;
    private Organizations organizations;
    private String userPreferredLanguage;

    public String maskedResponse() {
        return "IntrospectionResponse{" +
            "active=" + active +
            ", scope='" + scope + '\'' +
            ", clientId='" + clientId + '\'' +
            ", username='" + username.replaceAll("(?<=.{4}).", "*") + '\'' +
            ", tokenType='" + tokenType + '\'' +
            ", exp=" + exp +
            ", sub='" + sub + '\'' +
            ", iss='" + iss + '\'' +
            ", identityType=" + identityType +
            ", deviceType='" + deviceType + '\'' +
            ", organizations=" + organizations +
            ", userPreferredLanguage='" + userPreferredLanguage + '\'' +
            '}';
    }


    /**
     * An enumeration that indicates the type of identity.
     */
    public enum IdentityTypeEnum {
        DEVICE("device"),
        USER("user"),
        SERVICE("service"),
        CLIENT("client");

        private final String value;

        IdentityTypeEnum(String value) {
            this.value = value;
        }

        public static IdentityTypeEnum fromValue(String input) {
            for (IdentityTypeEnum b : IdentityTypeEnum.values()) {
                if (b.value.equals(input)) {
                    return b;
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

    }
}
