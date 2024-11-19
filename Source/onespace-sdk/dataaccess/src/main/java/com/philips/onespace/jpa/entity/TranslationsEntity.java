/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: TranslationsEntity.java
 */

package com.philips.onespace.jpa.entity;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name="translations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"table_name", "column_name", "record_id", "language_code"})})
public class TranslationsEntity {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(name = "translation_id")
    private UUID translationId;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "column_name", nullable = false)
    private String columnName;

    @Column(name = "record_id", nullable = false)
    private UUID recordId;

    @Column(name = "language_code", nullable = false)
    private String languageCode;

    @Column(name = "translated_text", nullable = false)
    private String translatedText;
}
