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
public class ProductKey {
    /**
     * Product & Product suite added as product key
     */
    public Item item;
    public CommonLicenseAttributes commonLicenseAttributes;
    public NamedUsers namedUsers;
}