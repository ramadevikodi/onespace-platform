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
public class Customer {
    /**
     * Sentinel auto-generated unique identifier of the customer.
     */
    public String id;
    /**
     * Name of the customer
     * example: Apollo_Hospitals
     */
    public String name;
    /**
     * Unique external identifier that maps to customer ID registered in OneSpace
     */
    public String externalId;
}