/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AppNotificationServiceImpl.java
 */

package com.philips.onespace.notification.serviceImpl;

import static com.philips.onespace.logging.LoggingAspect.logData;
import static com.philips.onespace.util.DateUtil.formatDateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philips.onespace.dto.Notification;
import com.philips.onespace.exception.CommonException;
import com.philips.onespace.jpa.entity.ActionEntity;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.entity.NotificationEntity;
import com.philips.onespace.jpa.entity.NotificationRecipientEntity;
import com.philips.onespace.jpa.repository.ActionRepository;
import com.philips.onespace.jpa.repository.AppNotificationRepository;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.mapper.NotificationMapper;
import com.philips.onespace.model.AppNotificationEvent;
import com.philips.onespace.model.Group;
import com.philips.onespace.model.User;
import com.philips.onespace.notification.service.AppNotificationService;
import com.philips.onespace.service.IamService;
import com.philips.onespace.util.Category;
import com.philips.onespace.util.ErrorMessages;
import com.philips.onespace.util.EventType;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppNotificationServiceImpl implements AppNotificationService {

	@Autowired
	private AppNotificationRepository appNotificationRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private NotificationMapper mapper;

	@Autowired
	private IamService iamService;

	@Autowired
	private ActionRepository actionRepository;

	/**
	 * This method saves App notification.
	 *
	 * @param notification
	 * @return Notification
	 */
	@Override
	public Notification saveAppNotification(Notification notification) throws BadRequestException {
		Notification response = null;
		try {
			// Semantic validations
			validateDateFields(notification);
			Optional<ApplicationEntity> applicationEntityOpt = applicationRepository
					.findById(UUID.fromString(notification.getSource().getId()));
			if (!applicationEntityOpt.isPresent()) {
				throw new BadRequestException(ErrorMessages.ERR_SOURCE_APPLICATION_DOESNOT_EXIST);
			}

			NotificationEntity notificationEntity = appNotificationRepository
					.save(mapper.dtoToEntity(notification, applicationEntityOpt.get()));
			response = mapper.entityToDto(notificationEntity);
		} catch (Exception expObj) {
			logData("Exception: saveAppNotification, Exception Details: ", expObj);
			throw expObj;
		}
		return response;
	}

	/**
	 * This method retrieves all the app notifications.
	 *
	 * @return List of Notification
	 */
	@Override
	public List<Notification> getAppNotifications(String userId) {
		List<Notification> responseList = null;
		try {
			List<NotificationEntity> appNotificationEntities = appNotificationRepository.findAllWithinExpiryDate();
			// Prepare entities based on recipients configured
			List<NotificationEntity> appNotificationEntitiesResult = appNotificationEntities.stream()
					.filter(entity -> entity.getRecipientEntities() == null || entity.getRecipientEntities().isEmpty()
							|| entity.getRecipientEntities().stream().map(temp -> {
								return String.valueOf(temp.getRecipientId());
							}).collect(Collectors.toList()).contains(userId))
					.collect(Collectors.toList());
			responseList = mapper.mapAppNotificationDTOs(appNotificationEntitiesResult, userId);
		} catch (Exception expObj) {
			logData("Exception: getAppNotifications, Exception Details: ", expObj);
			throw expObj;
		}
		return responseList;
	}

	/**
	 * This method deletes all the expired app notification.
	 *
	 * @return List of Notification
	 */
	@Override
	public void deleteExpiredAppNotifications() {
		try {
			appNotificationRepository.deleteExpiredAppNotifications(LocalDateTime.now());
		} catch (Exception expObj) {
			logData("Exception: deleteExpiredAppNotifications, Exception Details: ", expObj);
		}
	}

	/**
	 * This method marks the app notification as read.
	 *
	 * @param the    notification identifiers
	 * @param userId
	 * @return List of Notification
	 */
	@Override
	public void updateAppNotificationAsRead(List<UUID> notificationIdentifiers, UUID userId) throws BadRequestException {
		try {
			//Semantic validations
			long notificationCount = appNotificationRepository.getNotificationCount(notificationIdentifiers);
			if(notificationCount != notificationIdentifiers.size()) {
				throw new BadRequestException(ErrorMessages.INVALID_NOTIFICATION_ID);
			}
			appNotificationRepository.updateNotRecByAppNotIdAndUserId(notificationIdentifiers, userId, Boolean.TRUE);
		} catch (Exception expObj) {
			logData("Exception: updateAppNotificationAsRead, Exception Details: ", expObj);
			throw expObj;
		}
	}

	/**
	 * This method retrieves all the app notifications for the given notIds.
	 *
	 * @param the    notification identifiers
	 * @param userId
	 * @return List of Notification
	 */
	@Override
	@Transactional
	public List<Notification> getAppNotificationsByNotIds(List<UUID> notificationIdentifiers, String userId) {
		List<Notification> responseList = null;
		try {
			List<NotificationEntity> appNotificationEntities = appNotificationRepository
					.findAllByNotIdsAndExpiryDate(notificationIdentifiers);
			// Prepare entities based on recipients configured
			List<NotificationEntity> appNotificationEntitiesResult = appNotificationEntities.stream()
					.filter(entity -> entity.getRecipientEntities() == null || entity.getRecipientEntities().isEmpty()
							|| entity.getRecipientEntities().stream().map(temp -> {
								return String.valueOf(temp.getRecipientId());
							}).collect(Collectors.toList()).contains(userId))
					.collect(Collectors.toList());
			responseList = mapper.mapAppNotificationDTOs(appNotificationEntitiesResult, userId);
		} catch (Exception expObj) {
			logData("Exception: getAppNotificationsByNotIds, Exception Details: ", expObj);
			throw expObj;
		}
		return responseList;
	}

	/**
	 * This method triggers notifications for various events.
	 * 
	 * The process involves the following steps:
	 * 
	 * 1. **Event Processing**: The method accepts a notification event that
	 * includes details such as the application ID and event type. It processes
	 * these details to determine the appropriate notification action.
	 * 
	 * 2. **Notification Triggering**: Based on the event type and application ID,
	 * the method triggers the relevant app notification. This may involve sending
	 * app notifications to the appropriate recipients or systems.
	 * 
	 * @param notificationEvent An object containing the application ID and event
	 *                          type to be used for triggering the notification.
	 */
	@Override
	public void triggerAppNotification(AppNotificationEvent notificationEvent) throws CommonException {
		try {
			UUID applicationId = notificationEvent.getApplicationId();
			EventType eventType = notificationEvent.getEventType();
			switch (eventType) {
				case AWAITING_BUSINESS_OWNER_APPROVAL:
					sendAppRegisteredNotification(applicationId);
					break;
				case ACTION_INITIATED:
					sendActionInitiatedNotification(notificationEvent.getActionId());
					break;
				default:
					log.info("Case not available for the event type");
			}
		} catch (CommonException expObj) {
			logData("Exception: triggerAppNotification, Exception Details: ", expObj);
			throw expObj;
		}
	}

	@Transactional
	private void sendAppRegisteredNotification(UUID applicationId) {
		try {
			List<User> userList = new ArrayList<User>();
			Optional<ApplicationEntity> applicationEntityOpt = applicationRepository.findById(applicationId);
			if (applicationEntityOpt.isPresent()) {
				ApplicationEntity applicationEntity = applicationEntityOpt.get();
				List<Group> groups = iamService.getGroups(String.valueOf(applicationEntityOpt.get()
						.getBusinessUnitEntity().getBusinessUnitExtSystemEntity().getHspIamOrgId()));
				for (Group group : groups) {
					List<User> users = group.getUsers();
					if (null != users) {
						userList.addAll(users);
					}
				}
				saveNotificationEntity(applicationEntityOpt.get(), userList,
						"Application " + applicationEntity.getName() + " has been newly registered",
						"Application " + applicationEntity.getName() + " has been registered by "
								+ applicationEntity.getRegisteredBy() + " belonging to organization "
								+ applicationEntity.getBusinessUnitEntity().getName(),
						Category.INFORMATIONAL.toString());
			}
		} catch (Exception expObj) {
			logData("Exception: sendAppRegisteredNotification, Exception Details: ", expObj);
			throw expObj;
		}
	}

	private void saveNotificationEntity(ApplicationEntity applicationEntity, List<User> userList, String title,
			String message, String category) {
		NotificationEntity notificationEntity = NotificationEntity.builder().title(title).message(message)
				.category(category).createdAt(applicationEntity.getRegisteredDateTime())
				.expiry(applicationEntity.getRegisteredDateTime().plusDays(7)).application(applicationEntity).build();
		List<NotificationRecipientEntity> recipientList = userList.stream().map(entry -> {
			return NotificationRecipientEntity.builder().notification(notificationEntity)
					.recipientId(UUID.fromString(entry.getId())).read(Boolean.FALSE).build();
		}).collect(Collectors.toList());
		notificationEntity.setRecipientEntities(recipientList);
		appNotificationRepository.save(notificationEntity);
	}

	private void validateDateFields(Notification notification) throws BadRequestException {
		if (formatDateTime(notification.getCreatedAt()).isAfter(LocalDateTime.now())) {
			throw new BadRequestException(ErrorMessages.INVALID_DATE_TIME);
		}
		if (null != notification.getExpiry() && !formatDateTime(notification.getExpiry()).isAfter(LocalDateTime.now())) {
			throw new BadRequestException(ErrorMessages.INVALID_EXPIRY_DATE);
		}
	}

	@Transactional
	private void sendActionInitiatedNotification(UUID actionId) {
		Optional<ActionEntity> actionEntityOptional = actionRepository.findById(actionId);
		if (actionEntityOptional.isPresent()) {
			ActionEntity actionEntity = actionEntityOptional.get();
			NotificationEntity notificationEntity = NotificationEntity.builder()
					.title(actionEntity.getTitle())
					.message(actionEntity.getMessage())
					.category(actionEntity.getType())
					.createdAt(actionEntity.getDateTime())
					.expiry(actionEntity.getExpiryDateTime())
					.application(actionEntity.getApplication()).build();
			List<NotificationRecipientEntity> recipientList = actionEntity.getActionOwnersEntities()
					.stream().map(entry -> NotificationRecipientEntity.builder().notification(notificationEntity)
							.recipientId(entry.getPotentialOwner()).read(Boolean.FALSE).build()).toList();
			notificationEntity.setRecipientEntities(recipientList);
			appNotificationRepository.save(notificationEntity);
			logData("Notification generated for action  ", actionId);
		}
	}
}
