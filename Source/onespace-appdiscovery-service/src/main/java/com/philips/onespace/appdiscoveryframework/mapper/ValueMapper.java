/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ValueMapper.java
 */

package com.philips.onespace.appdiscoveryframework.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.philips.onespace.dto.Value;
import com.philips.onespace.jpa.entity.ApplicationStatusEntity;
import com.philips.onespace.jpa.entity.CategoryEntity;
import com.philips.onespace.jpa.entity.DeploymentEntity;
import com.philips.onespace.jpa.entity.LanguageEntity;
import com.philips.onespace.jpa.entity.ModalityEntity;
import com.philips.onespace.jpa.entity.SpecialityEntity;

@Mapper(componentModel = "spring")
public interface ValueMapper {

    default List<Value> mapLanguages(List<LanguageEntity> languageEntity) {
        return languageEntity.stream().map(c -> Value.builder().id(c.getId())
                .name(c.getDescription()).build()).collect(Collectors.toList());
    }

    default List<Value> mapSpecialities(List<SpecialityEntity> specialityEntities) {
        return specialityEntities.stream().map(c -> Value.builder().id(c.getId())
                .name(c.getSpecialityName()).description(c.getDescription()).build()).collect(Collectors.toList());
    }

    default List<Value> mapModalities(List<ModalityEntity> modalityEntities) {
        return modalityEntities.stream().map(c -> Value.builder().id(c.getId())
                .name(c.getModalityName()).description(c.getDescription()).build()).collect(Collectors.toList());
    }

    default List<Value> mapCategories(List<CategoryEntity> categoryEntities) {
        return categoryEntities.stream().map(c -> Value.builder().id(c.getId())
                .name(c.getName()).build()).collect(Collectors.toList());
    }

    default List<Value> mapApplicationStatus(List<ApplicationStatusEntity> applicationStatusEntities) {
        return applicationStatusEntities.stream().map(c -> Value.builder().id(c.getId())
                .name(c.getName()).build()).collect(Collectors.toList());
    }

    default List<Value> mapDeploymentModes(List<DeploymentEntity> deploymentEntities){
        return deploymentEntities.stream().map(c -> Value.builder().id(c.getId())
                .name(c.getMode()).build()).collect(Collectors.toList());
    }
}
