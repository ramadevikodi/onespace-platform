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

import com.philips.onespace.dto.EmailNotification;
import com.philips.onespace.notification.service.EmailManager;
import com.philips.onespace.notification.service.EmailNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {
	@Autowired
	private EmailManager emailManager;
	@Override
	public boolean sendEmailNotification(EmailNotification emailNotification, boolean sendTemplatedEmail) {
		return emailManager.sendEmail(emailNotification, sendTemplatedEmail);
	}
}
