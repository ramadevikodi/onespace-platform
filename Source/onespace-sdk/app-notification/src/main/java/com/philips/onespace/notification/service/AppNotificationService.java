/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AppNotificationService.java
 */

package com.philips.onespace.notification.service;

import java.util.List;
import java.util.UUID;

import org.apache.coyote.BadRequestException;

import com.philips.onespace.dto.Notification;
import com.philips.onespace.exception.CommonException;
import com.philips.onespace.model.AppNotificationEvent;

public interface AppNotificationService {

	/**
	 * This method saves App notification.
	 *
	 * @param notification
	 * @return Notification
	 */
	public Notification saveAppNotification(Notification notification) throws BadRequestException;

	/**
	 * This method retrieves all the app notifications.
	 *
	 * @return List of Notification
	 */
	public List<Notification> getAppNotifications(String userId);

	/**
	 * This method deletes all the expired app notifications.
	 *
	 */
	public void deleteExpiredAppNotifications();

	/**
	 * This method marks the app notification as read.
	 *
	 * @param the    notification identifiers
	 * @param userId
	 * @return List of Notification
	 */
	public void updateAppNotificationAsRead(List<UUID> notificationIdentifiers, UUID userId) throws BadRequestException;

	/**
	 * This method retrieves all the app notifications for the given notification
	 * identifiers.
	 *
	 * @param the    notification identifiers
	 * @param userId
	 * @return List of Notification
	 */
	public List<Notification> getAppNotificationsByNotIds(List<UUID> notificationIdentifiers, String userId);

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
	public void triggerAppNotification(AppNotificationEvent notificationEvent) throws CommonException;

}
