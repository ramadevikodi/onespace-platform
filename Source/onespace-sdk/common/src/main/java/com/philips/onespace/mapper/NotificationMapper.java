/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NotificationMapper.java
 */

package com.philips.onespace.mapper;

import java.util.List;

import com.philips.onespace.dto.Notification;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.entity.NotificationEntity;

public interface NotificationMapper {

	/**
	 * This method maps from entity to DTO.
	 *
	 * @param appNotificationEntity
	 * @return AppNotificationDTO
	 */
	Notification entityToDto(NotificationEntity appNotificationEntity);

	/**
	 * This method maps from DTO to entity.
	 *
	 * @param appNotificationDTO
	 * @return AppNotificationEntity
	 */
	NotificationEntity dtoToEntity(Notification appNotificationDTO, ApplicationEntity applicationEntity);
    
    /**
	 * This method maps from entity to DTO.
	 *
	 * @param appNotificationEntities
	 * @return Lis of AppNotificationDTO
	 */
    List<Notification> mapAppNotificationDTOs(List<NotificationEntity> appNotificationEntities, String userId);
	
}
