/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ActionMapper.java
 */

package com.philips.onespace.appdiscoveryframework.mapper;

import static com.philips.onespace.util.DateUtil.formatDateTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.philips.onespace.dto.Action;
import com.philips.onespace.dto.Source;
import com.philips.onespace.jpa.entity.ActionEntity;
import com.philips.onespace.jpa.entity.ActionOwnersEntity;

@Mapper(componentModel = "spring")
public interface ActionMapper {

    @Mapping(target = "dateTime", source = "dateTime", qualifiedByName = "mapDateTimeToString")
    @Mapping(target = "expiryDateTime", source = "expiryDateTime", qualifiedByName = "mapDateTimeToString")
    @Mapping(target = "dueDateTime", source = "dueDateTime", qualifiedByName = "mapDateTimeToString")
    @Mapping(target = "source", source = "actionEntity", qualifiedByName = "mapSource")
    @Mapping(target = "completedAtDateTime", source = "actionOwnersEntities", qualifiedByName = "mapCompletedAtDateTime")
    @Mapping(target = "potentialOwner", source = "actionOwnersEntities", qualifiedByName = "mapPotentialOwners")
    @Mapping(target = "status", source = "actionOwnersEntities", qualifiedByName = "mapStatus")
    Action entityToDto(ActionEntity actionEntity, @Context UUID potentialOwner);

    @Mapping(target = "actionOwnersEntities", ignore = true)
    @Mapping(target = "dateTime", ignore = true)
    @Mapping(target = "dueDateTime", ignore = true)
    @Mapping(target = "expiryDateTime", ignore = true)
    ActionEntity dtoToEntity(Action action);

    @Mapping(target = "actionEntity", source = "actionEntity")
    ActionOwnersEntity toActionOwnersEntity(UUID potentialOwner, String status, ActionEntity actionEntity);

    default List<ActionOwnersEntity> mapActionOwnersEntity(List<UUID> potentialOwners, String status, ActionEntity actionEntity) {
        return potentialOwners.stream()
                .map(owner -> toActionOwnersEntity(owner, status, actionEntity))
                .toList();
    }


    @Named("mapPotentialOwners")
    default List<UUID> mapPotentialOwners(List<ActionOwnersEntity> actionOwnersEntities, @Context UUID potentialOwner) {
        return actionOwnersEntities.stream()
                .distinct()
                .map(ActionOwnersEntity::getPotentialOwner)
                .toList();
    }

    @Named("mapCompletedAtDateTime")
    default String mapCompletedAtDateTime(List<ActionOwnersEntity> actionOwnersEntities, @Context UUID potentialOwner) {
        LocalDateTime completedAtDateTime = actionOwnersEntities.stream()
                .filter(entity -> entity.getPotentialOwner().equals(potentialOwner))
                .map(ActionOwnersEntity::getCompletedAtDateTime)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        if (completedAtDateTime != null) {
            return mapDateTimeToString(completedAtDateTime);
        }
        return null;
    }

    @Named("mapStatus")
    default String mapStatus(List<ActionOwnersEntity> actionOwnersEntities, @Context UUID potentialOwner) {
        return actionOwnersEntities.stream()
                .filter(entity -> entity.getPotentialOwner().equals(potentialOwner))
                .map(ActionOwnersEntity::getStatus)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Named("mapSource")
    default Source mapSource(ActionEntity actionEntity) {
        if (actionEntity.getApplication() != null) {
            return Source.builder()
                    .id(String.valueOf(actionEntity.getApplication().getId()))
                    .name(actionEntity.getApplication().getName()).build();
        }
        return null;
    }

    @Named("mapDateTimeToString")
    default String mapDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return formatDateTime(localDateTime);
        } else return null;
    }

}
