/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: EmailNotificationService.java
 */

package com.philips.onespace.notification.service;

import com.philips.onespace.dto.EmailNotification;

public interface EmailManager {

	boolean sendEmail(EmailNotification emailNotification, boolean sendTemplatedEmail);

	
}
