/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AppNotificationRepository.java
 */

package com.philips.onespace.jpa.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.philips.onespace.jpa.entity.NotificationEntity;

import jakarta.transaction.Transactional;

public interface AppNotificationRepository extends JpaRepository<NotificationEntity, UUID> {
	
	/**
	 * This method gets all the app notifications within expiry date.
	 *
	 * @return NotificationEntity list
	 */
	@Query("FROM NotificationEntity ne WHERE ne.expiry >= CURRENT_TIMESTAMP")
	List<NotificationEntity> findAllWithinExpiryDate();
	
	/**
	 * This method delets all the expired app notifications.
	 *
	 * @param currentDate
	 */
	@Modifying
	@Transactional
	@Query("DELETE from NotificationEntity ne WHERE ne.expiry < :currentDate")
	void deleteExpiredAppNotifications(LocalDateTime currentDate);
	
	/**
	 * This method updates the notification as read for the given recdipient.
	 *
	 * @param notIds
	 * @param userId
	 * @param readFlag
	 */
	@Modifying
	@Transactional
	@Query("UPDATE NotificationRecipientEntity nre SET nre.read=:readFlag WHERE nre.notification.id IN :notIds and nre.recipientId=:userId")
	void updateNotRecByAppNotIdAndUserId(List<UUID> notIds, UUID userId, Boolean readFlag);
	
	/**
	 * This method gets all the app notifications within expiry date, for the given notIds.
	 *
	 * @return NotificationEntity list
	 */
	@Query("FROM NotificationEntity ne WHERE ne.expiry >= CURRENT_TIMESTAMP and ne.id IN :notIds")
	List<NotificationEntity> findAllByNotIdsAndExpiryDate(List<UUID> notIds);
	
	/**
	 * This method gets all the app notifications  total for the given notIds.
	 *
	 * @return NotificationEntity list
	 */
	@Query("SELECT COUNT(ne) FROM NotificationEntity ne WHERE ne.id IN :notIds")
	long getNotificationCount(List<UUID> notIds);
	
}
