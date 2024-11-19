/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: LanguageEntity.java
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
import lombok.Data;
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
@Data
@Table(name="language")
public class LanguageEntity {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(name = "language_id")
    private UUID id;

    @Column(name = "language_code")
    @Size(max = 256)
    private String languageCode;

    @Column(name = "description")
    @Size(max = 512)
    private String description;

    @Column(name = "date_format")
    @Size(min=3, max = 256)
    private String dateFormat;

    @Column(name = "time_format")
    @Size(min=3, max = 256)
    private String timeFormat;

    @Column(name = "currency_format")
    @Size(min=3, max = 256)
    private String currencyFormat;

    @JsonIgnore
    @ManyToMany(mappedBy = "languages", cascade = {CascadeType.ALL})
    private List<ApplicationEntity> applicationEntities;

}