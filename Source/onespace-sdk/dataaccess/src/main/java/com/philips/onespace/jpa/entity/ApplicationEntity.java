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
import java.time.ZonedDateTime;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "application")
public class ApplicationEntity {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(name = "application_id")
    private UUID id;

    @Column(name = "application_name", unique = true)
    @Size(max = 256)
    private String name;

    @Column(name = "short_description")
    @Size(min=3)
    private String shortDescription;

    @Column(name = "long_description")
    @Size(min=3)
    private String longDescription;

    @Column(name = "url")
    @Size(max = 256)
    private String url;

    @Column(name = "registered_by")
    @Size(max = 120)
    private String registeredBy;

    @Column(name = "registered_datetime")
    private LocalDateTime registeredDateTime;

    @Column(name = "last_modified_datetime")
    private LocalDateTime lastModifiedDateTime;

    @Column(name = "icon")
    @Size(max = 256)
    private String icon;

    @Column(name = "banner")
    private String banner;

    @Column(name = "version")
    @Size(max = 50)
    private String version;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "application_speciality_mapping",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "speciality_id"))
    private List<SpecialityEntity> specialities;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "application_modality_mapping",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "modality_id"))
    private List<ModalityEntity> modalities;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "application_language_mapping",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    private List<LanguageEntity> languages;

    @Column(name = "owner_organization")
    private UUID ownerOrganization;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false, name = "owner_organization")
    private BusinessUnitEntity businessUnitEntity;

    @Column(name = "approver")
    @Size(max = 256)
    private String approver;

    @Column(name = "l1_comments")
    @Size(max = 512)
    private String l1_comments;

    @Column(name = "cost_center")
    @Size(max = 50)
    private String costCenter;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(name = "l2_comments")
    @Size(max = 512)
    private String l2_comments;

    @Column(name = "published_datetime")
    private ZonedDateTime publishedDateTime;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "application_proposition_mapping",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "proposition_id"))
    private List<PropositionEntity> propositions;

    @Column(name = "is_mfe")
    private boolean isMFE;

    @Column(name = "is_sso_enabled")
    private boolean isSSOEnabled;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private ApplicationStatusEntity status;

    @ManyToOne
    @JoinColumn(name = "deployment_id")
    private DeploymentEntity deployment;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "application_market_mapping",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "market_id"))
    private List<MarketEntity> markets;

    @Transient
    private boolean enabledForOrg = true;

    @Transient
    private boolean enabledForUser = true;

    @Column(name = "promote_as_upcoming_app")
    private boolean promoteAsUpcomingApp;

    @Column(name = "promote_as_newapp")
    private boolean promoteAsNewApp;

    @Column(name = "is_third_party")
    private boolean isThirdParty;
}
