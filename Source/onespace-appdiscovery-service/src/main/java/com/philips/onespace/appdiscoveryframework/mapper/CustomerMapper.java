/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: CustomerMapper.java
 */

package com.philips.onespace.appdiscoveryframework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.philips.onespace.dto.Customer;
import com.philips.onespace.jpa.entity.CustomerEntity;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "market", source = "marketEntity")
    Customer entityToDto(CustomerEntity customerEntity);

    @Mapping(target = "marketEntity", source = "market")
    CustomerEntity dtoToEntity(Customer customer);
}
