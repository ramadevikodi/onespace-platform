package com.philips.onespace.notification;

import com.philips.onespace.dto.EmailNotification;
import com.philips.onespace.notification.serviceImpl.SesEmailManager;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;
import software.amazon.awssdk.services.sesv2.model.SendEmailResponse;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SesEmailManagerTest {

    @Mock
    private SesV2Client sesClientMock;

    private SesEmailManager sesEmailManager;

    private static final String accessKey = "dummyAccessKey";
    private static final String key = "dummyKey";
    private static final String senderEmail = "sender@example.com";

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Instantiate SesEmailManager with dummy credentials
        sesEmailManager = new SesEmailManager(accessKey, key, "us-east-1", senderEmail, "testConfigSet");

        // Inject the mocked sesClient into sesEmailManager using reflection
        ReflectionTestUtils.setField(sesEmailManager, "sesv2Client", sesClientMock);
    }

    @Test
    public void testSendEmail_Success() {
        // Arrange
        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setTo(Arrays.asList("recipient@example.com"));
        emailNotification.setCc(Arrays.asList("cc@example.com"));
        emailNotification.setSubject("Test Subject");
        emailNotification.setMessage("Test Message");

        // Mock the sendEmail method to return a successful result
        when(sesClientMock.sendEmail(any(SendEmailRequest.class))).thenReturn(SendEmailResponse.builder().build());

        // Act
        boolean result = sesEmailManager.sendEmail(emailNotification, true);

        // Assert
        assertTrue(result, "Email should be sent successfully");
    }

    @Test
    public void testSendEmail_Failure() {
        // Arrange
        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setTo(Arrays.asList("recipient@example.com"));
        emailNotification.setCc(Arrays.asList("cc@example.com"));
        emailNotification.setSubject("Test Subject");
        emailNotification.setMessage("Test Message");

        // Mock the sendEmail method to throw an exception
        when(sesClientMock.sendEmail(any(SendEmailRequest.class)))
                .thenThrow(new RuntimeException("SES service error"));

        // Act
        boolean result = sesEmailManager.sendEmail(emailNotification, true);

        // Assert
        assertFalse(result, "Email sending should fail and return false");
    }
}
