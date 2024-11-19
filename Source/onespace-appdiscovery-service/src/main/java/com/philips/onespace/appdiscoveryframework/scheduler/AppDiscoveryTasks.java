/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AppDiscoveryTasks.java
 */

package com.philips.onespace.appdiscoveryframework.scheduler;

import static com.philips.onespace.logging.LoggingAspect.logData;

import com.philips.onespace.appdiscoveryframework.service.interfaces.ActionService;
import com.philips.onespace.notification.service.AppNotificationService;
import com.philips.onespace.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AppDiscoveryTasks {

	@Autowired
	private AppNotificationService appNotificationService;

	@Autowired
	private ActionService actionService;

	/**
	 * This method schedules task for deleting expired notifications.
	 *
	 */
	//@Scheduled(cron = "0 * * * * *")
	@Scheduled(cron = Constants.DELETE_NOTIFICATION_CRON_EXP)
	public void deleteExpiredNotifications() {
	    logData("Delete expired app notification started at:=",System.currentTimeMillis());
	    try {
	    	appNotificationService.deleteExpiredAppNotifications();
	    } catch (Exception expObj) {
	    	logData("Exception: deleteExpiredNotifications, Exception Details: ", expObj);
		}
	    logData("Delete expired app notification completed at:"+System.currentTimeMillis());
	}

	@Scheduled(cron = "${app.discovery.cron.mark-expired-actions}")// Run every one hour
	public String markExpiredActions() {
		int updatedRows = actionService.markExpiredActions();
		if (updatedRows == 0)
			return "No expired actions";
		else
			return "Marked expired actions";
	}
}
