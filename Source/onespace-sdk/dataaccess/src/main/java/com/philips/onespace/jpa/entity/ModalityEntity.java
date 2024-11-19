/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ModalityEntity.java
 */

package com.philips.onespace.jpa.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "modality")
public class ModalityEntity {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(name = "modality_id")
    private UUID id;

    @Column(name = "modality_name")
    @Size(max = 256)
    private String modalityName;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "modalities", cascade = {CascadeType.ALL})
    private List<ApplicationEntity> applicationEntities;
}
