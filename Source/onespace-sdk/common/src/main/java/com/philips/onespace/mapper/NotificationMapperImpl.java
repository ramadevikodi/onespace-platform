/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NotificationMapperImpl.java
 */

package com.philips.onespace.mapper;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.philips.onespace.dto.Notification;
import com.philips.onespace.dto.Source;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.entity.NotificationEntity;
import com.philips.onespace.jpa.entity.NotificationRecipientEntity;
import com.philips.onespace.util.DateUtil;

@Component
public class NotificationMapperImpl implements NotificationMapper {

	/**
	 * This method maps from entity to DTO.
	 *
	 * @param appNotificationEntity
	 * @return AppNotificationDTO
	 */
	@Override
	public Notification entityToDto(NotificationEntity appNotificationEntity) {
		Notification notification = new Notification();
		notification.setId(appNotificationEntity.getId());
		notification.setTitle(appNotificationEntity.getTitle());
		notification.setMessage(appNotificationEntity.getMessage());
		Source source = Source.builder().id(String.valueOf(appNotificationEntity.getApplication().getId())).name(appNotificationEntity.getApplication().getName()).build();
		notification.setSource(source);
		notification.setCategory(appNotificationEntity.getCategory());
		notification.setCreatedAt(DateUtil.formatDateTime(appNotificationEntity.getCreatedAt()));
		notification.setExpiry(DateUtil.formatDateTime(appNotificationEntity.getExpiry()));
		if(null != appNotificationEntity.getRecipientEntities() && appNotificationEntity.getRecipientEntities().size() > 0) {
			List<String> recList = new ArrayList<String>(10);
			for (NotificationRecipientEntity notificationRecipientEntity : appNotificationEntity.getRecipientEntities()) {
				recList.add(String.valueOf(notificationRecipientEntity.getRecipientId()));
			}
			notification.setRecipients(recList);
		}
		return notification;
	}

	/**
	 * This method maps from DTO to entity.
	 *
	 * @param appNotificationDTO
	 * @param applicationEntity
	 * @return AppNotificationEntity
	 */
	@Override
	public NotificationEntity dtoToEntity(Notification appNotificationDTO, ApplicationEntity applicationEntity) {
		NotificationEntity entity = new NotificationEntity();
		entity.setTitle(appNotificationDTO.getTitle());
		entity.setMessage(appNotificationDTO.getMessage());
		entity.setCategory(appNotificationDTO.getCategory());
		entity.setCreatedAt(DateUtil.formatDateTime(appNotificationDTO.getCreatedAt()));
		ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
		entity.setExpiry(null != appNotificationDTO.getExpiry() ? DateUtil.formatDateTime(appNotificationDTO.getExpiry()) : utcDateTime.toLocalDateTime().plusDays(7));
		if (null != appNotificationDTO.getRecipients() && appNotificationDTO.getRecipients().size() > 0) {
			List<NotificationRecipientEntity> notificationRecipientEntitiesList = new ArrayList<NotificationRecipientEntity>(10);
			for (String recipient : appNotificationDTO.getRecipients()) {
				NotificationRecipientEntity recipientEntity = new NotificationRecipientEntity();
				recipientEntity.setRead(Boolean.FALSE);
				recipientEntity.setRecipientId(UUID.fromString(recipient));
				recipientEntity.setNotification(entity);
				notificationRecipientEntitiesList.add(recipientEntity);
			}
			entity.setRecipientEntities(notificationRecipientEntitiesList);
		}
		entity.setApplication(applicationEntity);
		return entity;
	}

	/**
	 * This method maps from entity to DTO.
	 *
	 * @param appNotificationEntities
	 * @return List of AppNotificationDTO
	 */
	@Override
	public List<Notification> mapAppNotificationDTOs(List<NotificationEntity> appNotificationEntities, String userId) {
		List<Notification> notifications = new ArrayList<Notification>(10);
		for (NotificationEntity notificationEntity : appNotificationEntities) {
			Notification notification = new Notification();
			notification.setId(notificationEntity.getId());
			notification.setTitle(notificationEntity.getTitle());
			notification.setMessage(notificationEntity.getMessage());
			Source source = Source.builder().id(String.valueOf(notificationEntity.getApplication().getId())).name(notificationEntity.getApplication().getName()).build();
			notification.setSource(source);
			notification.setCategory(notificationEntity.getCategory());
			notification.setCreatedAt(DateUtil.formatDateTime(notificationEntity.getCreatedAt()));
			notification.setExpiry(DateUtil.formatDateTime(notificationEntity.getExpiry()));
			if(null != notificationEntity.getRecipientEntities() && notificationEntity.getRecipientEntities().size() > 0) {
				List<String> recList = new ArrayList<String>(10);
				for (NotificationRecipientEntity notificationRecipientEntity : notificationEntity.getRecipientEntities()) {
					recList.add(String.valueOf(notificationRecipientEntity.getRecipientId()));
					if(notificationRecipientEntity.getRecipientId().equals(UUID.fromString(userId))) {
						notification.setRead(notificationRecipientEntity.getRead());
					}
				}
				notification.setRecipients(recList);
			}
			notifications.add(notification);
		}
		return notifications;
	}

}
