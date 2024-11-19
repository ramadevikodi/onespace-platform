/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ApplicationMapper.java
 */

package com.philips.onespace.appdiscoveryframework.mapper;

import com.philips.onespace.dto.Application;
import com.philips.onespace.dto.ApplicationFilter;
import com.philips.onespace.dto.Approval;
import com.philips.onespace.dto.BusinessUnit;
import com.philips.onespace.dto.Deployment;
import com.philips.onespace.dto.Market;
import com.philips.onespace.dto.Ownership;
import com.philips.onespace.dto.Proposition;
import com.philips.onespace.dto.Registration;
import com.philips.onespace.dto.Value;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.entity.ApplicationStatusEntity;
import com.philips.onespace.jpa.entity.CategoryEntity;
import com.philips.onespace.jpa.entity.DeploymentEntity;
import com.philips.onespace.jpa.entity.LanguageEntity;
import com.philips.onespace.jpa.entity.MarketEntity;
import com.philips.onespace.jpa.entity.ModalityEntity;
import com.philips.onespace.jpa.entity.PropositionEntity;
import com.philips.onespace.jpa.entity.SpecialityEntity;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.jpa.repository.ApplicationStatusRepository;
import com.philips.onespace.jpa.repository.BusinessUnitRepository;
import com.philips.onespace.jpa.repository.CategoryRepository;
import com.philips.onespace.jpa.repository.DeploymentRepository;
import com.philips.onespace.jpa.repository.LanguageRepository;
import com.philips.onespace.jpa.repository.MarketRepository;
import com.philips.onespace.jpa.repository.ModalityRepository;
import com.philips.onespace.jpa.repository.PropositionRepository;
import com.philips.onespace.jpa.repository.SpecialityRepository;
import com.philips.onespace.util.LocaleUtil;
import jakarta.persistence.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ApplicationMapper {
    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;
    @Autowired
    private SpecialityRepository specialityRepository;
    @Autowired
    private ModalityRepository modalityRepository;
    @Autowired
    private DeploymentRepository deploymentRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PropositionRepository propositionRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private LocaleUtil localeUtil;

    public ApplicationEntity dtoToEntityForRegistration(ApplicationEntity applicationEntity, Application application) {
        setRegistration(applicationEntity, application);
        setStatus(applicationEntity, application); // Status can be either DRAFT / REGISTERED. Hence, reading from input DTO
        setDeployment(applicationEntity, application);
        return applicationEntity;
    }

    public ApplicationEntity dtoToEntityForL1Approval(ApplicationEntity applicationEntity, Application application) {
        setStatus(applicationEntity, application);
        applicationEntity.setLastModifiedDateTime(LocalDateTime.now());
        // TODO :: Fetch list of approvers from IAM
        if (application.getOwnership() != null) {
            applicationEntity.setL1_comments(application.getOwnership().getL1comments());
            applicationEntity.setCostCenter(application.getOwnership().getCostCenter());
        }
        return applicationEntity;
    }

    public ApplicationEntity dtoToEntityForL2Approval(ApplicationEntity applicationEntity, Application application) {
        setStatus(applicationEntity, application);
        applicationEntity.setLastModifiedDateTime(ZonedDateTime.now().toLocalDateTime());
        if (application.getApproval() != null) {
            applicationEntity.setPublishedDateTime(application.getApproval().getPublishedDateTime());
            applicationEntity.setL2_comments(application.getApproval().getL2comments());
            setProposition(applicationEntity,application);
            setMarkets(applicationEntity,application);
        }
        return applicationEntity;
    }

    public List<Application> entityToDto(Page<ApplicationEntity> applicationEntities) {
        if (!applicationEntities.isEmpty()) {
            return applicationEntities.stream().map(c -> entityToDto(c)).collect(Collectors.toList());
        }
        return null;
    }

    private void setProposition(ApplicationEntity applicationEntity, Application application) {
        List<Proposition> propositions = application.getApproval().getPropositions();
        if (propositions != null && !propositions.isEmpty()) {
            applicationEntity.setPropositions(getPropositionEntities(propositions));
        } else {
            applicationEntity.setPropositions(null);
        }
    }

    private void setMarkets(ApplicationEntity applicationEntity, Application application) {
        List<Market> markets = application.getApproval().getMarkets();
        if (markets != null && !markets.isEmpty()) {
            applicationEntity.setMarkets(getMarketEntities(markets));
        } else {
            applicationEntity.setMarkets(null);
        }
    }

        private void setDeployment (ApplicationEntity applicationEntity, Application application){
            if (application.getDeployment() != null) {
                applicationEntity.setUrl(application.getDeployment().getUrl());
                applicationEntity.setIcon(application.getDeployment().getIcon());
                if (application.getDeployment().getMode() != null) {
                    applicationEntity.setDeployment(getDeploymentEntity(application));
                } else {
                    applicationEntity.setDeployment(null);
                }
            }
        }

    private void setStatus(ApplicationEntity applicationEntity, Application application) {
        if(application.getStatus() != null) {
            applicationEntity.setStatus(getApplicationStatusEntity(application));
        } else {
            applicationEntity.setStatus(null);
        }
    }

    private void setRegistration(ApplicationEntity applicationEntity, Application application) {
        if(application.getRegistration() != null) {
            applicationEntity.setName(application.getRegistration().getName());
            applicationEntity.setShortDescription(application.getRegistration().getShortDescription());
            applicationEntity.setVersion(application.getRegistration().getVersion());
            List<Value> specialities = application.getRegistration().getSpecialities();
            if(specialities != null && !specialities.isEmpty()) {
                applicationEntity.setSpecialities(getSpecialityEntities(specialities));
            } else {
                applicationEntity.setSpecialities(null);
            }
            List<Value> modalities = application.getRegistration().getModalities();
            if(modalities != null && !modalities.isEmpty()) {
                applicationEntity.setModalities(getModalityEntities(modalities));
            } else {
                applicationEntity.setModalities(null);
            }
            if (!StringUtils.isEmpty(application.getRegistration().getLongDescription())) {
                applicationEntity.setLongDescription(application.getRegistration().getLongDescription());
            }
            List<Value> languages = application.getRegistration().getLanguages();
            if(languages != null && !languages.isEmpty()) {
                applicationEntity.setLanguages(getLanguageEntities(languages));
            } else {
                applicationEntity.setLanguages(null);
            }
            BusinessUnit businessUnit = application.getRegistration().getBusinessUnit();
            if(businessUnit != null) {
                applicationEntity.setOwnerOrganization(businessUnitRepository.findById(businessUnit.getId()).get().getId());
            }
            Value category = application.getRegistration().getCategory();
            if(category != null) {
                applicationEntity.setCategory(getCategoryEntity(category));
            }

        }
    }

    public Application entityToDto(ApplicationEntity applicationEntity) {
        if (applicationEntity != null) {
            String shortDescription = applicationEntity.getShortDescription();
            String applicationName = applicationEntity.getName();
            String statusName = null;
            Application application = Application.builder()
                    .id(applicationEntity.getId())
                    .lastModifiedDateTime(applicationEntity.getLastModifiedDateTime())
                    .enabledForOrg(applicationEntity.isEnabledForOrg())
                    .enabledForUser(applicationEntity.isEnabledForUser())
                    .isMFE(applicationEntity.isMFE())
                    .ssoEnabled(applicationEntity.isSSOEnabled())
                    .promoteAsUpComingApp(applicationEntity.isPromoteAsUpcomingApp())
                    .promoteAsNewApp(applicationEntity.isPromoteAsNewApp())
                    .isThirdParty(applicationEntity.isThirdParty())
                    .build();
            String languageCode = localeUtil.getLocale().getLanguage();
                if (localeUtil.isLocaleValid(languageCode)) {
                    List<String> translatedList = getTranslations(applicationEntity.getId(), languageCode);
                    if (!translatedList.isEmpty()) {
                        Map<String, String> extractedValues = new HashMap<>();
                        for (String value : translatedList) {
                            String[] parts = value.split(",");
                            if (parts.length >= 2) {
                                extractedValues.put(parts[0], parts[1]);
                            }
                        }
                        shortDescription = extractedValues.get("description");
                        applicationName = extractedValues.get("application_name");
                        statusName = extractedValues.get("status_name");
                    }
                }
                Registration registration = Registration.builder()
                        .name(applicationName == null ? applicationEntity.getName() : applicationName)
                        .shortDescription(shortDescription == null ? applicationEntity.getShortDescription() : shortDescription)
                        .longDescription(applicationEntity.getLongDescription())
                        .version(applicationEntity.getVersion())
                        .registeredBy(applicationEntity.getRegisteredBy())
                        .registeredDateTime(applicationEntity.getRegisteredDateTime())
                        .ownerOrganization(applicationEntity.getOwnerOrganization())
                        .build();
                if (null != applicationEntity.getModalities() && !applicationEntity.getModalities().isEmpty()) {
                    registration.setModalities(getModalities(applicationEntity));
                }
                if (null != applicationEntity.getSpecialities() && !applicationEntity.getSpecialities().isEmpty()) {
                    registration.setSpecialities(getSpecialities(applicationEntity));
                }
                if (null != applicationEntity.getLanguages() && !applicationEntity.getLanguages().isEmpty()) {
                    registration.setLanguages(getLanguages(applicationEntity));
                }
                if (applicationEntity.getCategory() != null) {
                    registration.setCategory(getCategory(applicationEntity));
                }
                application.setRegistration(registration);
            if (applicationEntity.getStatus() != null) {
                application.setStatus(statusName == null ? getStatus(applicationEntity) :
                        getTranslatedStatus(applicationEntity, statusName));
            }
            if (applicationEntity.getDeployment() != null) {
                application.setDeployment(Deployment.builder()
                        .url(applicationEntity.getUrl())
                        .icon(applicationEntity.getIcon())
                        .banner(StringUtils.isEmpty(applicationEntity.getBanner()) ? null : applicationEntity.getBanner())
                        .mode(getDeploymentMode(applicationEntity.getDeployment()))
                        .build());
            }
            if (!StringUtils.isBlank(applicationEntity.getCostCenter())) {
                application.setOwnership(Ownership.builder()
                        .costCenter(applicationEntity.getCostCenter())
                        .l1comments(StringUtils.isEmpty(applicationEntity.getL1_comments()) ? null : applicationEntity.getL1_comments())
                        .build());
            }
            if (applicationEntity.getBusinessUnitEntity() != null) {
                application.getRegistration().setBusinessUnit(BusinessUnit.builder()
                        .id(applicationEntity.getBusinessUnitEntity().getId())
                        .cluster(applicationEntity.getBusinessUnitEntity().getCluster())
                        .businessSegments(applicationEntity.getBusinessUnitEntity().getBusinessSegments())
                        .name(applicationEntity.getBusinessUnitEntity().getName())
                        .description(applicationEntity.getBusinessUnitEntity().getDescription())
                        .hspIamOrgId(applicationEntity.getBusinessUnitEntity().getBusinessUnitExtSystemEntity().getHspIamOrgId())
                        .build());
            }

            if (applicationEntity.getPropositions() != null && !applicationEntity.getPropositions().isEmpty()) {
                application.setApproval(Approval.builder()
                        .propositions(getPropositionList(applicationEntity))
                        .markets(getMarketList(applicationEntity))
                        .approver(applicationEntity.getApprover())
                        .l2comments(StringUtils.isEmpty(applicationEntity.getL2_comments()) ? null : applicationEntity.getL2_comments())
                        .publishedDateTime(applicationEntity.getPublishedDateTime())
                        .build());
            }
            return application;
        }
        else {
            return null;
        }
    }

    private List<String> getTranslations(UUID recordId, String languageCode) {
        return applicationRepository
                .getApplicationTranslations(recordId,
                        languageCode);
    }

    private static Value getDeploymentMode (DeploymentEntity deploymentEntity){
            return Value.builder().id(deploymentEntity.getId()).name(deploymentEntity.getMode()).build();
        }
        private static Value getStatus (ApplicationEntity applicationEntity){
            return Value.builder().id(applicationEntity.getStatus().getId()).name(applicationEntity.getStatus().getName()).build();
        }

    private static Value getCategory(ApplicationEntity applicationEntity) {
        return Value.builder().id(applicationEntity.getCategory().getId()).name(applicationEntity.getCategory().getName()).build();
    }

    private static List<Value> getModalities(ApplicationEntity applicationEntity) {
        return applicationEntity.getModalities().stream().map(c -> Value.builder().id(c.getId()).name(c.getModalityName()).build()).collect(Collectors.toList());
    }

    private static List<Proposition> getPropositionList (ApplicationEntity applicationEntity){
        return applicationEntity.getPropositions().stream().map(c -> Proposition.builder().id(c.getId()).name(c.getName()).description(c.getDescription()).build()).collect(Collectors.toList());
    }

    private static List<Market> getMarketList (ApplicationEntity applicationEntity){
        return applicationEntity.getMarkets().stream().map(c -> Market.builder().marketId(c.getMarketId()).marketName(c.getMarketName()).region(c.getRegion()).country(c.getCountry()).subRegion(c.getSubRegion()).build()).collect(Collectors.toList());
    }

        private static List<Value> getSpecialities (ApplicationEntity applicationEntity){
            return applicationEntity.getSpecialities().stream().map(c -> Value.builder().id(c.getId()).name(c.getSpecialityName()).build()).collect(Collectors.toList());
        }

    private static List<Value> getLanguages(ApplicationEntity applicationEntity) {
        return applicationEntity.getLanguages().stream().map(c -> Value.builder().id(c.getId()).name(c.getLanguageCode()).build()).collect(Collectors.toList());
    }

    private List<LanguageEntity> getLanguageEntities(List<Value> languages) {
        return languages.stream()
                .map(s -> languageRepository.findById(s.getId()).get())
                .collect(Collectors.toList());
    }

        private List<ModalityEntity> getModalityEntities (List < Value > modalities) {
            return modalities.stream()
                    .map(s -> modalityRepository.findById(s.getId()).get())
                    .collect(Collectors.toList());
        }

        private List<PropositionEntity> getPropositionEntities (List<Proposition> propositions) {
            return propositions.stream()
                    .map(s -> propositionRepository.findById(s.getId()).get())
                    .collect(Collectors.toList());
        }

    private List<MarketEntity> getMarketEntities (List<Market> markets) {
        return markets.stream()
                .map(s -> marketRepository.findById(s.getMarketId()).get())
                .collect(Collectors.toList());
    }

    private List<SpecialityEntity> getSpecialityEntities(List<Value> specialityList) {
        return specialityList.stream()
                .map(s -> specialityRepository.findById(s.getId()).get())
                .collect(Collectors.toList());
    }

    public ApplicationStatusEntity getApplicationStatusEntity(Application application) {
        return applicationStatusRepository.findById(application.getStatus().getId()).get();
    }

    private DeploymentEntity getDeploymentEntity(Application application) {
        return deploymentRepository.findById(application.getDeployment().getMode().getId()).get();
    }

    private CategoryEntity getCategoryEntity(Value category) {
        return categoryRepository.findById(category.getId()).get();
    }

    private static Value getTranslatedStatus (ApplicationEntity applicationEntity, String StatusName){
        return Value.builder().id(applicationEntity.getStatus().getId()).name(StatusName).build();
    }

    public List<ApplicationFilter> convertToApplicationFilter(List<Tuple> tuples) {
        return tuples.stream()
                .map(tuple -> ApplicationFilter.builder()
                        .key(tuple.get("key", String.class))
                        .value(tuple.get("value", String[].class))
                        .type(getType(tuple))
                        .build())
                .collect(Collectors.toList());
    }

    private String getType(Tuple tuple) {
        boolean isBoolean = true;
        String []  values = tuple.get("value", String[].class);
        for (String value : values) {
            if (!value.equalsIgnoreCase("true")
                    && !value.equalsIgnoreCase("false")) {
                isBoolean = false;
                break;
            }
        }
        return isBoolean ? "checkbox" : "dropdown";
    }

}
