/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AppNotificationController.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import static com.philips.onespace.logging.LoggingAspect.logData;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.coyote.BadRequestException;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.philips.onespace.appdiscoveryframework.util.SecurityContextUtil;
import com.philips.onespace.dto.Notification;
import com.philips.onespace.logging.LogEmittedDataService;
import com.philips.onespace.notification.service.AppNotificationService;
import com.philips.onespace.util.Constants;
import com.philips.onespace.validator.ApiVersion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Cleanup;

@Validated
@RequestMapping("/Notification")
@RestController
public class AppNotificationController {

	private static final String APP_NOTIFICATION_EMITTER_NAME = "SSE_EMITTER_APP_NOTIFICATIONS";
	private static final long APP_NOTIFICATION_INITIAL_DELAY_CRON = 60000;
	
	@Value("${sse.appnotification.cron}")
	private String notificationCron ;

	@Autowired
	private AppNotificationService appNotificationService;

	@Autowired
	private SecurityContextUtil contextUtil;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private LogEmittedDataService logEmittedDataService;

	private boolean running = true;
	private Map<String, SseEmitter> notificationsEmitters = new HashMap<>();

	/**
	 * Save app notification.
	 *
	 * @param notification the notification DTO
	 * @return the posted notification with unique ID.
	 * @throws BadRequestException 
	 */
	@ApiVersion("1")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Notification> saveAppNotification(@Valid @RequestBody Notification notification) throws BadRequestException {
		Notification response = appNotificationService.saveAppNotification(notification);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * Get app notifications.
	 *
	 * @return the an array of notification objects.
	 */
	@ApiVersion("1")
	@RequestMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, method = RequestMethod.GET)
	public SseEmitter getAppNotifications() throws BadRequestException {
		// Send all notification for the first time
		SseEmitter notificationEmitter = new SseEmitter(0L);
		String userId = contextUtil
				.getUserIdFromPrincipal(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		notificationsEmitters.put(userId, notificationEmitter);
		List<Notification> response = appNotificationService.getAppNotifications(userId);
		try {
			//Set the emitted data for logging before sending
			logEmittedDataService.setName(APP_NOTIFICATION_EMITTER_NAME);
			logEmittedDataService.setData(response);

			notificationEmitter.send(
					SseEmitter.event().name(APP_NOTIFICATION_EMITTER_NAME).data(response, MediaType.APPLICATION_JSON));
		} catch (Exception expObj) {
			logData("Exception in getAppNotifications: details", expObj);
		}
		return notificationEmitter;
	}

	/**
	 * Mark app notification as read.
	 *
	 * @param the notification identifier
	 * @return the no content status code
	 */
	@ApiVersion("1")
	@RequestMapping(value = "/$Read", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<String> updateAppNotificationAsRead(@Valid @RequestBody @NotEmpty(message = "missing_notification_id_param") List<UUID> notificationIdentifiers) throws BadRequestException {
		appNotificationService.updateAppNotificationAsRead(notificationIdentifiers, UUID.fromString(contextUtil
				.getUserIdFromPrincipal(SecurityContextHolder.getContext().getAuthentication().getPrincipal())));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Schedule the listening app notification channel.
	 *
	 */
	@Scheduled(initialDelay = APP_NOTIFICATION_INITIAL_DELAY_CRON, fixedDelay = Long.MAX_VALUE)
	public void startListeningToPgChannel() throws IOException, InterruptedException {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			PGConnection pgConnection = connection.unwrap(PGConnection.class);
			try (Statement statement = connection.createStatement()) {
				statement.execute("LISTEN " + Constants.PG_CHANNEL);
			}
			while (running) {
				try {
					PGNotification[] notArr = pgConnection.getNotifications();
					Set<UUID>  notificationIdentifiers = new HashSet<UUID>(10);
					if (notArr.length > 0) {
						for (PGNotification pgNotification : notArr) {
							notificationIdentifiers.add(UUID.fromString(pgNotification.getParameter()));
						}
					}
					sendDeltaNotifications(new ArrayList<UUID>(notificationIdentifiers));
					// Sleep to prevent busy-waiting
					Thread.sleep(Long.parseLong(notificationCron));
				} catch (InterruptedException expObj) {
					Thread.currentThread().interrupt();
					running = false;
				}
			}
		} catch (Exception expObj) {
			logData("Exception in sendPeriodicAppNotifications: details", expObj);
		}
	}

	/**
	 * Send the delta notifications for all the sse emitter.
	 *
	 * @param notificationIdentifiers
	 */
	private void sendDeltaNotifications(List<UUID> notificationIdentifiers) throws IOException {
		for (Map.Entry<String, SseEmitter> entry : notificationsEmitters.entrySet()) {
			SseEmitter notificationEmitter = entry.getValue();
			String userId = entry.getKey();
			try {
				List<Notification> response = appNotificationService
						.getAppNotificationsByNotIds(notificationIdentifiers, userId);
				if (null != response && response.size() > 0) {
					notificationEmitter.send(SseEmitter.event().name(APP_NOTIFICATION_EMITTER_NAME).data(response));
				} else {
					notificationEmitter.send(SseEmitter.event().name(Constants.SSE_KEEP_ALIVE));
				}
			} catch (IllegalStateException expObj) {
				notificationEmitter.completeWithError(expObj);
				notificationsEmitters.remove(userId);
			} catch (Exception expObj) {
				notificationEmitter.completeWithError(expObj);
				notificationsEmitters.remove(userId);
			}
		}
	}

}
