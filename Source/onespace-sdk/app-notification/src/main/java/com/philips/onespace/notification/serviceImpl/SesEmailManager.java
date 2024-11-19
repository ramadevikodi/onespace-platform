package com.philips.onespace.notification.serviceImpl;

import static com.philips.onespace.logging.LoggingAspect.logData;

import com.google.gson.Gson;
import com.philips.onespace.dto.EmailNotification;
import com.philips.onespace.notification.service.EmailManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.Destination;
import software.amazon.awssdk.services.sesv2.model.EmailContent;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;
import software.amazon.awssdk.services.sesv2.model.Template;
import software.amazon.awssdk.services.sesv2.model.Content;
import software.amazon.awssdk.services.sesv2.model.Message;
import software.amazon.awssdk.services.sesv2.model.Body;

@Component
public class SesEmailManager implements EmailManager {
    private SesV2Client sesv2Client;
    private String senderEmail;
    private String configSetName;
    public SesEmailManager(@Value("${aws.accessKey}") String accessKey, @Value("${aws.secretKey}") String secretKey,
                           @Value("${aws.region}") String region,
                           @Value("${aws.senderEmail}") String senderEmail,
                           @Value("${aws.sesConfigSetName}") String configSetName) {
        sesv2Client = SesV2Client.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .region(Region.of(region))
                .build();
        this.senderEmail = senderEmail;
        this.configSetName = configSetName;
    }
    @Override
    public boolean sendEmail(EmailNotification emailNotification, boolean sendTemplatedEmail) {
        EmailContent content = null;
        if(sendTemplatedEmail) {
            content = createTemplatedEmailContent(emailNotification);
        } else {
            content = createSimpleEmailContent(emailNotification);
        }
        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(
                        Destination.builder()
                                .toAddresses(emailNotification.getTo())
                                .ccAddresses(emailNotification.getCc())
                                .build())
                .content(content)
                .fromEmailAddress(senderEmail)
                .configurationSetName(configSetName)
                .build();
        try {
            logData("Attempting to send an email for user = " + emailNotification.getTo());
            sesv2Client.sendEmail(emailRequest);
        } catch (Exception ex) {
            logData("The email was not sent. Error message: " + ex.getMessage());
            return false;
        }
        logData("The email was successfully sent.");
        return true;
    }

    private static EmailContent createSimpleEmailContent(EmailNotification emailNotification) {
        return EmailContent.builder()
                .simple(Message.builder()
                        .subject(Content.builder()
                                .data(emailNotification.getSubject())
                                .build())
                        .body(Body.builder()
                                .text(Content.builder()
                                        .data(emailNotification.getMessage())
                                        .build())
                                .build())
                        .build())
                .build();
    }

    private static EmailContent createTemplatedEmailContent(EmailNotification emailNotification) {
        return EmailContent.builder()
                .template(Template.builder()
                        .templateName(String.valueOf(emailNotification.getType()))
                        .templateData(new Gson().toJson(emailNotification.getMetadata()))
                        .build())
                .build();
    }
}
