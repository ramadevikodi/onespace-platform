/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AppNotificationServiceImplTest.java
 */

package com.philips.onespace.notification.serviceImpl;

import com.philips.onespace.dto.Notification;
import com.philips.onespace.dto.Source;
import com.philips.onespace.jpa.entity.*;
import com.philips.onespace.jpa.repository.ActionRepository;
import com.philips.onespace.jpa.repository.AppNotificationRepository;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.mapper.NotificationMapper;
import com.philips.onespace.model.AppNotificationEvent;
import com.philips.onespace.model.Group;
import com.philips.onespace.model.User;
import com.philips.onespace.service.IamService;
import com.philips.onespace.util.ErrorMessages;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.philips.onespace.util.Constants.DATE_FORMAT_WITHOUT_TIMEZONE;
import static com.philips.onespace.util.EventType.ACTION_INITIATED;
import static com.philips.onespace.util.EventType.AWAITING_BUSINESS_OWNER_APPROVAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppNotificationServiceImplTest {
    @InjectMocks
    private AppNotificationServiceImpl appNotificationService;

    @Mock
    private AppNotificationRepository appNotificationRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private NotificationMapper mapper;

    @Mock
    private IamService iamService;

    private List<UUID> notificationIdentifiers;
    private UUID userId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        notificationIdentifiers = List.of(UUID.randomUUID(), UUID.randomUUID());
        userId = UUID.randomUUID();
    }

    @Test
    void testSaveAppNotification() throws BadRequestException {

        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_TIMEZONE);
        String formattedDateTime = dateTime.format(formatter);

        Notification notification = new Notification();
        notification.setCreatedAt(formattedDateTime);
        Source source = new Source();
        source.setId("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");
        notification.setSource(source);

        ApplicationEntity applicationEntity = new ApplicationEntity();
        NotificationEntity notificationEntity = new NotificationEntity();

        when(applicationRepository.findById(UUID.fromString(notification.getSource().getId()))).thenReturn(Optional.of(applicationEntity));
        when(appNotificationRepository.save(mapper.dtoToEntity(notification, applicationEntity))).thenReturn(notificationEntity);
        when(mapper.entityToDto(notificationEntity)).thenReturn(notification);

        Notification result = appNotificationService.saveAppNotification(notification);
        assertNotNull(result);

    }


    @Test
    void testSaveAppNotification_negative() {

        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_TIMEZONE);
        String formattedDateTime = dateTime.format(formatter);

        Notification notification = new Notification();
        notification.setCreatedAt(formattedDateTime);

        Source source = new Source();
        source.setId("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");
        notification.setSource(source);

        // !applicationEntityOpt.isPresent
        assertThrows(BadRequestException.class, () -> {
                    appNotificationService.saveAppNotification(notification);
                }
        );
    }


    @Test
    void testSaveAppNotification_negative_1() {

        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_TIMEZONE);
        String formattedDateTime = dateTime.format(formatter);

        Notification notification = new Notification();
        notification.setCreatedAt(formattedDateTime);
        assertThrows(BadRequestException.class, () -> {
                    appNotificationService.saveAppNotification(notification);
                }
        );

    }

    @Test
    void testSaveAppNotification_negative_2() {

        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_TIMEZONE);
        String formattedDateTime = dateTime.format(formatter);

        Notification notification = new Notification();
        notification.setCreatedAt(formattedDateTime);
        notification.setExpiry(formattedDateTime);

        assertThrows(BadRequestException.class, () -> {
                    appNotificationService.saveAppNotification(notification);
                }
        );

    }


    @Test
    void testSaveAppNotification_negative_3() {

        LocalDateTime now = LocalDateTime.now();
        Notification notification = new Notification();
        notification.setCreatedAt(String.valueOf(now));

        assertThrows(DateTimeParseException.class, () -> {
                    appNotificationService.saveAppNotification(notification);
                }
        );

    }

    @Test
    void testGetAppNotifications() {

        String userId1 = "user1";

        List<Notification> notification = new ArrayList<>();


        List<NotificationEntity> appNotificationEntities = new ArrayList<>();
        NotificationEntity notificationEntity = new NotificationEntity();
        List<NotificationRecipientEntity> notificationRecipientEntities = new ArrayList<>();
        NotificationRecipientEntity notificationRecipientEntity = new NotificationRecipientEntity();
        notificationRecipientEntity.setId(UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054"));
        notificationRecipientEntities.add(notificationRecipientEntity);
        notificationEntity.setRecipientEntities(notificationRecipientEntities);
        appNotificationEntities.add(notificationEntity);
        List<NotificationEntity> appNotificationEntitiesResult = new ArrayList<>();


        when(appNotificationRepository.findAllWithinExpiryDate()).thenReturn(appNotificationEntities);

        when(mapper.mapAppNotificationDTOs(appNotificationEntitiesResult, userId1)).thenReturn(notification);

        List<Notification> result = appNotificationService.getAppNotifications(userId1);
        assertNotNull(result);
    }

    @Test
    void testDeleteExpiredAppNotifications() {
        // Arrange
        doNothing().when(appNotificationRepository).deleteExpiredAppNotifications(any(LocalDateTime.class));

        // Act
        appNotificationService.deleteExpiredAppNotifications();

        // Assert
        verify(appNotificationRepository, times(1)).deleteExpiredAppNotifications(any(LocalDateTime.class));
    }

    @Test
    void testDeleteExpiredAppNotifications_Exception() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(appNotificationRepository).deleteExpiredAppNotifications(any(LocalDateTime.class));

        // Act & Assert
        assertDoesNotThrow(() -> appNotificationService.deleteExpiredAppNotifications());
    }

    @Test
    void testUpdateAppNotificationAsRead() throws BadRequestException {
        // Arrange
        when(appNotificationRepository.getNotificationCount(notificationIdentifiers)).thenReturn((long) notificationIdentifiers.size());
        doNothing().when(appNotificationRepository).updateNotRecByAppNotIdAndUserId(notificationIdentifiers, userId, Boolean.TRUE);

        // Act
        appNotificationService.updateAppNotificationAsRead(notificationIdentifiers, userId);

        // Assert
        verify(appNotificationRepository, times(1)).getNotificationCount(notificationIdentifiers);
        verify(appNotificationRepository, times(1)).updateNotRecByAppNotIdAndUserId(notificationIdentifiers, userId, Boolean.TRUE);
    }

    @Test
    void testUpdateAppNotificationAsRead_InvalidNotificationId() {
        // Arrange
        when(appNotificationRepository.getNotificationCount(notificationIdentifiers)).thenReturn(0L);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            appNotificationService.updateAppNotificationAsRead(notificationIdentifiers, userId);
        });

        assertEquals(ErrorMessages.INVALID_NOTIFICATION_ID, exception.getMessage());
    }

    @Test
    void testUpdateAppNotificationAsRead_Exception() {


        // Arrange
        when(appNotificationRepository.getNotificationCount(notificationIdentifiers)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            appNotificationService.updateAppNotificationAsRead(notificationIdentifiers, userId);
        });

        assertEquals("Database error", exception.getMessage());
    }


    @Test
    void testGetAppNotificationsByNotIds() {

        String userId1 = "user2";
        List<Notification> notification = new ArrayList<>();


        List<NotificationEntity> appNotificationEntities = new ArrayList<>();
        NotificationEntity notificationEntity = new NotificationEntity();
        List<NotificationRecipientEntity> notificationRecipientEntities = new ArrayList<>();
        NotificationRecipientEntity notificationRecipientEntity = new NotificationRecipientEntity();
        notificationRecipientEntity.setId(UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054"));
        notificationRecipientEntities.add(notificationRecipientEntity);
        notificationEntity.setRecipientEntities(notificationRecipientEntities);
        appNotificationEntities.add(notificationEntity);
        List<NotificationEntity> appNotificationEntitiesResult = new ArrayList<>();


        when(appNotificationRepository.findAllByNotIdsAndExpiryDate(notificationIdentifiers)).thenReturn(appNotificationEntities);
        when(mapper.mapAppNotificationDTOs(appNotificationEntitiesResult, userId1)).thenReturn(notification);

        List<Notification> result = appNotificationService.getAppNotificationsByNotIds(notificationIdentifiers, userId1);
        assertNotNull(result);

    }

    @Test
    void testTriggerAppNotification() {

        UUID uuid = UUID.randomUUID();


        AppNotificationEvent notificationEvent = new AppNotificationEvent();
        notificationEvent.setApplicationId(uuid);
        notificationEvent.setEventType(AWAITING_BUSINESS_OWNER_APPROVAL);

        ApplicationEntity applicationEntity = new ApplicationEntity();
        BusinessUnitEntity businessUnitEntity = new BusinessUnitEntity();
        BusinessUnitExtSystemEntity businessUnitExtSystemEntity = new BusinessUnitExtSystemEntity();
        businessUnitExtSystemEntity.setHspIamOrgId(UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054"));
        businessUnitEntity.setBusinessUnitExtSystemEntity(businessUnitExtSystemEntity);
        applicationEntity.setBusinessUnitEntity(businessUnitEntity);
        applicationEntity.setName("abc");
        applicationEntity.setRegisteredBy("person");
        applicationEntity.setOwnerOrganization(uuid);
        applicationEntity.setRegisteredDateTime(LocalDateTime.now());

        List<Group> groups = new ArrayList<>();
        Group group = new Group();

        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId("f4cfcff2-f77f-4e5a-bb2f-8ee157845052");
        user.setUserName("user1");
        users.add(user);
        group.setUsers(users);
        groups.add(group);

        when(applicationRepository.findById(uuid)).thenReturn(Optional.of(applicationEntity));
        when(iamService.getGroups(String.valueOf(applicationEntity.getBusinessUnitEntity().getBusinessUnitExtSystemEntity().getHspIamOrgId()))).thenReturn(groups);

         appNotificationService.triggerAppNotification(notificationEvent);

    }

    @Test
    void testTriggerAppNotification_ACTION_INITIATED() {

        UUID uuid = UUID.randomUUID();


        AppNotificationEvent notificationEvent = new AppNotificationEvent();
        notificationEvent.setActionId(uuid);
        notificationEvent.setEventType(ACTION_INITIATED);

        List<ActionOwnersEntity> actionOwnersEntities = new ArrayList<>();
        ActionOwnersEntity actionOwnersEntity = new ActionOwnersEntity();
        actionOwnersEntities.add(actionOwnersEntity);
        ActionEntity actionEntity = new ActionEntity();
        actionEntity.setTitle("Action");
        actionEntity.setMessage("Action Massage");
        actionEntity.setType("Action Type");
        actionEntity.setActionOwnersEntities(actionOwnersEntities);
        actionEntity.setExpiryDateTime(LocalDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.MIDNIGHT));
        actionEntity.setDateTime(LocalDateTime.now());
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setTitle(actionEntity.getTitle());


        when(actionRepository.findById(uuid)).thenReturn(Optional.of(actionEntity));

        appNotificationService.triggerAppNotification(notificationEvent);
    }
}