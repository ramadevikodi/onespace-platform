package com.philips.onespace.appdiscoveryframework.controller;
import org.apache.coyote.BadRequestException;
import com.philips.onespace.dto.EmailNotificationType;
import com.philips.onespace.notification.service.EmailNotificationService;
import com.philips.onespace.dto.EmailNotification;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


class EmailNotificationControllerTest {

    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private EmailNotificationController emailNotificationController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    Boolean active = Boolean.TRUE;

    @Test
    void testSendEmailNotification_Success() throws Exception {

        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setType(EmailNotificationType.APP_REGISTERED);
        when(emailNotificationService.sendEmailNotification(emailNotification, active)).thenReturn(active);


        ResponseEntity responseEntity = emailNotificationController.sendEmailNotification(emailNotification);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(emailNotificationService, times(1)).sendEmailNotification(emailNotification, active);
    }

    @Test
    void testSendEmailNotification_MissingType_And_MissingSubjectOrMessage_ThrowsBadRequestException() {

        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setType(null);
        emailNotification.setSubject(null);
        emailNotification.setMessage(null);

        assertThrows(BadRequestException.class, () -> {
            emailNotificationController.sendEmailNotification(emailNotification);
        });

        verify(emailNotificationService, never()).sendEmailNotification(any(), anyBoolean());
    }


}
