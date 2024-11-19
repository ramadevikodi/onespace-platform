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
public class NamedUsers {
    /**
     * The list of named users who are entitled to use the product.
     * Note : This is applicable only for RMS Cloud connected mode.
     */
    public ArrayList<NamedUser> namedUser;
}