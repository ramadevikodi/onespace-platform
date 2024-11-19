package com.philips.onespace.sentinel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Entitlements{
    /**
     * Total number of records
     */
    public int count;
    /**
     * List of entitlements
     */
    public ArrayList<Entitlement> entitlement;
}