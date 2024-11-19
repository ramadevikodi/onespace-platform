package com.philips.onespace.sentinel.model;

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
public class Entitlement{
    /**
     * Sentinel auto-generated unique identifier of the entitlement.
     */
    public String id;
    /**
     * Product keys representing product suite / products within the entitlement
     */
    public ProductKeys productKeys;
}