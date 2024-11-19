/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ApplicationEntity.java
 */

package com.philips.onespace.jpa.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings(value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
@Entity
@Table(name = "app_notification")
public class NotificationEntity {

	@Id
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
	@Column(name = "id")
	private UUID id;

	@Column(name = "title")
	@Size(max = 1000)
	private String title;

	@Column(name = "message")
	private String message;

	@Column(name = "category")
	@Size(max = 200)
	private String category;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "expiry")
	private LocalDateTime expiry;
	
	@OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
	private List<NotificationRecipientEntity> recipientEntities;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source", nullable = true)
	private ApplicationEntity application;

}
