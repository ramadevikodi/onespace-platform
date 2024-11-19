/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: BusinessUnitEntity.java
 */

package com.philips.onespace.jpa.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "business_unit")
public class BusinessUnitEntity {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(name = "business_unit_id")
    private UUID id;

    @Column(name = "cluster")
    @Size(max = 256)
    private String cluster;

    @Column(name = "segment")
    @Size(max = 256)
    private String businessSegments;

    @Column(name = "business_unit_name")
    @Size(max = 256)
    private String name;

    @Size(max = 512)
    private String description;

    @OneToMany(mappedBy = "businessUnitEntity")
    private List<ApplicationEntity> applicationEntities;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "business_unit_id", referencedColumnName = "business_unit_id")
    private BusinessUnitExtSystemEntity businessUnitExtSystemEntity;
}
