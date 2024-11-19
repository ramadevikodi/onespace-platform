package com.philips.onespace.sentinel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * License attribute common across all product keys.
 * example: NAMED_USER_LICENSE=true, NO_OF_NAMEDUSERS=5
 */
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CommonLicenseAttribute {
    /**
     * Name of the license attribute common across all product keys.
     * The attribute names depend on licenses included in the product.
     *
     * For a network license, use PRIMARY_1_CRITERIA.
     * For a standalone license, use CLIENT_1_CRITERIA along with a NUM_CLIENT_LOCKED value to specify the number of client criteria. The default value of NUM_CLIENT_LOCKED is 1.
     * For a named license (for connected mode), use NAMED_USER_LICENSE (set to true) and NO_OF_NAMEDUSERS.
     * example: NAMED_USER_LICENSE
     */
    public String name;

    /**
     * Value of the license attribute common across all product keys.
     * example: true
     */
    public String value;
}