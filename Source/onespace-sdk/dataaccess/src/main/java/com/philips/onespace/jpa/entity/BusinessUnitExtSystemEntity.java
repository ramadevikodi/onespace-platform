/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: BusinessUnitExtSystemEntity.java
 */

package com.philips.onespace.jpa.entity;

import java.util.UUID;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "business_unit_external_system_mapping")
public class BusinessUnitExtSystemEntity {

    @Id
    @Column(name = "business_unit_id")
    private UUID id;

    @Column(name = "hsp_iam_org_id")
    private UUID hspIamOrgId;

    @Column(name = "sap_account_id")
    @Size(min=3, max = 255)
    private String sapAccountId;
}
