/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NotificationListener.java
 */

package com.philips.onespace.notification.event.listener;

import static com.philips.onespace.logging.LoggingAspect.logData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.philips.onespace.model.AppNotificationEvent;
import com.philips.onespace.notification.service.AppNotificationService;
import com.philips.onespace.util.Constants;

@Component
public class NotificationListener {

	@Autowired
	private AppNotificationService notificationService;
	
	@Async
    @EventListener
    public void handleAppNotificationEvent(AppNotificationEvent event) {
		try {
			Thread.sleep(Constants.NOTIFICATION_EVENT_LISTENER_DELAY);
			notificationService.triggerAppNotification(event);
		} catch (Exception expObj) {
			logData("Exception: handleAppNotificationEvent, Exception Details: ", expObj);
		}
    }
	
}
