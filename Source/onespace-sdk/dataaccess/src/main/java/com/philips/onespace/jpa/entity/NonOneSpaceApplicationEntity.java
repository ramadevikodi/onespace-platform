/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NonOneSpaceApplicationEntity.java
 */

package com.philips.onespace.jpa.entity;


import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "nononespace_application")
public class NonOneSpaceApplicationEntity {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", unique = true)
    @Size(max = 256)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "registeredBy")
    private String registeredBy;
    
    @Column(name = "app_order")
    private Integer order;
    
    @Column(name = "registered_datetime")
	private LocalDateTime registeredDateTime;
    
    @Column(name = "icon")
    private String icon;
    
    @Column(name = "customer_id")
    private UUID customerId;
    
    @ManyToOne
    @JoinColumn(insertable = false, updatable = false, name = "customer_id")
    private CustomerEntity customerEntity;
    
}
