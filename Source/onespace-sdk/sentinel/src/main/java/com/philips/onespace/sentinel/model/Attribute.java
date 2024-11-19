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
 * Attribute associated with license model.
 * example: FLOATING_USER_LIMIT=5
 */
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Attribute {
    /**
     * Name of the license model attribute.
     * example: FLOATING_USER_LIMIT
     */
    public String name;
    /**
     * Value of the license model attribute.
     * example: 5
     */
    public String value;
}