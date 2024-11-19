/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AppNotificationControllerTest.java
 */

package com.philips.onespace.appdiscoveryframework.controller;

import com.philips.onespace.appdiscoveryframework.util.SecurityContextUtil;
import com.philips.onespace.dto.Notification;

import java.sql.Connection;

import com.philips.onespace.logging.LogEmittedDataService;
import com.philips.onespace.notification.service.AppNotificationService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppNotificationControllerTest {

    @InjectMocks
    private AppNotificationController appNotificationController;

    private static final String APP_NOTIFICATION_EMITTER_NAME = "SSE_EMITTER_APP_NOTIFICATIONS";

    @Mock
    private AppNotificationService appNotificationService;

    @Mock
    private SecurityContextUtil contextUtil;

    @Mock
    private DataSource dataSource;

    @Mock
    private LogEmittedDataService logEmittedDataService;

    @Mock
    private Connection connection;

    @Mock
    private PGConnection pgConnection;

    @Mock
    private Statement statement;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        Object principal = "onespace";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.setContext(securityContext);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.unwrap(PGConnection.class)).thenReturn(pgConnection);
        when(connection.createStatement()).thenReturn(statement);

    }


    @Test
    void testSaveAppNotification() throws BadRequestException {
        Notification notification = new Notification();

        when(appNotificationService.saveAppNotification(notification)).thenReturn(notification);
        ResponseEntity<Notification> result = appNotificationController.saveAppNotification(notification);

        assertNotNull(result);
    }

    @Test
    void testGetAppNotifications() throws BadRequestException {

        List<Notification> notifications = new ArrayList<>();

        logEmittedDataService.setName(APP_NOTIFICATION_EMITTER_NAME);
        logEmittedDataService.setData(notifications);

        SseEmitter result = appNotificationController.getAppNotifications();

        assertNotNull(result);
    }

    @Test
    void testUpdateAppNotificationAsRead() throws BadRequestException {
        List<UUID> notificationIdentifiers = List.of(UUID.randomUUID(), UUID.randomUUID());
        UUID userId = UUID.randomUUID();

        when(contextUtil.getUserIdFromPrincipal(any())).thenReturn(userId.toString());

        ResponseEntity<String> response = appNotificationController.updateAppNotificationAsRead(notificationIdentifiers);

        verify(appNotificationService).updateAppNotificationAsRead(notificationIdentifiers, userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testStartListeningToPgChannel() throws Exception {

        PGNotification[] notifications = new PGNotification[]{
                mock(PGNotification.class)
        };
        when(pgConnection.getNotifications()).thenReturn(notifications);
        when(notifications[0].getParameter()).thenReturn(UUID.randomUUID().toString());

        Thread thread = new Thread(() -> {
            try {
                appNotificationController.startListeningToPgChannel();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        thread.interrupt();
        thread.join();
    }

}
