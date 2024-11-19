package com.philips.onespace.jpa.specification;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.entity.ApplicationStatusEntity;
import com.philips.onespace.jpa.entity.CategoryEntity;
import com.philips.onespace.jpa.entity.DeploymentEntity;
import com.philips.onespace.jpa.entity.LanguageEntity;
import com.philips.onespace.jpa.entity.MarketEntity;
import com.philips.onespace.jpa.entity.ModalityEntity;
import com.philips.onespace.jpa.entity.SpecialityEntity;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Component
public class ApplicationSpecifications {
   
    /**
     * Get the application entity specification
     * @param specification base specification
     * @param applicationEntities list of application entities
     * @return specification
     */
    public Specification<ApplicationEntity> getApplicationEntitySpecification(Specification specification,
                                                                              List<ApplicationEntity> applicationEntities) {
        List<ApplicationEntity> finalApplicationEntities = applicationEntities;
        return specification.and((root, query, cb) -> root.get("id")
                .in(finalApplicationEntities.stream().map(ApplicationEntity::getId).collect(Collectors.toList())));
    }

    /**
     * Build the specification based on the criteria
     * @param criteria filter criteria
     * @return specification
     */
    public Specification buildSpecification(Map<String, String> criteria) {
        if(criteria == null){
            return Specification.where(null);
        }
        Specification<ApplicationEntity> specification = Specification.where(null);
        if(criteria.containsKey("deployment")){
            String mode = criteria.get("deployment");
            Specification<ApplicationEntity> deploymentSpec = hasDeploymentMode(mode);
            if (deploymentSpec!=null) {
                specification = specification.and(deploymentSpec);
            }
        }
        if(criteria.containsKey("market")){
            String markets = criteria.get("market");
            Specification<ApplicationEntity> marketSpec = hasMarket(markets);
            if (marketSpec!=null) {
                specification = specification.and(marketSpec);
            }
        }
        if(criteria.containsKey("region")){
            String regions = criteria.get("region");
            Specification<ApplicationEntity> regionSpec = hasRegion(regions);
            if (regionSpec!=null) {
                specification = specification.and(regionSpec);
            }
        }
        if(criteria.containsKey("country")){
            String countries = criteria.get("country");
            Specification<ApplicationEntity> countrySpec = hasCountry(countries);
            if (countrySpec!=null) {
                specification = specification.and(countrySpec);
            }
        }
        if(criteria.containsKey("sub-region")){
            String subRegions = criteria.get("sub-region");
            Specification<ApplicationEntity> subRegionSpec = hasSubRegion(subRegions);
            if (subRegionSpec!=null) {
                specification = specification.and(subRegionSpec);
            }
        }
        if(criteria.containsKey("language")){
            String languages = criteria.get("language");
            Specification<ApplicationEntity> languageSpec = hasLanguage(languages);
            if (languageSpec!=null) {
                specification = specification.and(languageSpec);
            }
        }
        if(criteria.containsKey("sso-enabled")){
            String ssoEnabled = criteria.get("sso-enabled");
            Specification<ApplicationEntity> ssoEnabledSpec = hasSSOEnabled(ssoEnabled);
            if (ssoEnabledSpec!=null) {
                specification = specification.and(ssoEnabledSpec);
            }
        }
        if(criteria.containsKey("speciality")){
            String specialities = criteria.get("speciality");
            Specification<ApplicationEntity> specialitySpec = hasSpeciality(specialities);
            if (specialitySpec!=null) {
                specification = specification.and(specialitySpec);
            }
        }
        if(criteria.containsKey("modality")){
            String modalities = criteria.get("modality");
            Specification<ApplicationEntity> modalitySpec = hasModality(modalities);
            if (modalitySpec!=null) {
                specification = specification.and(modalitySpec);
            }
        }
        if(criteria.containsKey("category")){
            String categories = criteria.get("category");
            Specification<ApplicationEntity> categorySpec = hasCategory(categories);
            if (categorySpec!=null) {
                specification = specification.and(categorySpec);
            }
        }
        if(criteria.containsKey("statusName")){
            String status = criteria.get("statusName");
            Specification<ApplicationEntity> applicationStatusSpec = hasApplicationStatus(status);
            if (applicationStatusSpec!=null) {
                specification = specification.and(applicationStatusSpec);
            }
        }
        return specification;
    }

    /**
     * Build the query based on the deployment modes
     * @param modes deployment modes
     * @return specification
     */
    public Specification<ApplicationEntity> hasDeploymentMode(String modes) {
        return (root, query, cb) -> {
            if (modes == null || modes.isEmpty()) {
                return cb.conjunction();
            }
            Join<ApplicationEntity, DeploymentEntity> deploymentJoin = root
                    .join("deployment");
            Predicate predicate = cb.disjunction();
            if(modes.contains(",")){
                List<String> modeList = Arrays.asList(modes.split(","));
                for(String mode : modeList){
                    predicate = cb.or(predicate, cb.like(cb.lower(deploymentJoin
                            .get("mode")), "%" + mode.toLowerCase() + "%"));
                }
            }
            else{
                predicate = cb.like(cb.lower(deploymentJoin.get("mode")), "%" + modes.toLowerCase() + "%");
            }
            return predicate;
        };
    }

    /**
     * Build the query based on the market names
     * @param marketNames market names
     * @return specification
     */
    public Specification<ApplicationEntity> hasMarket(String marketNames) {
        return (root, query, cb) -> {
            if (marketNames == null || marketNames.isEmpty()) {
                return cb.conjunction();
            }
            Join<ApplicationEntity, MarketEntity> marketEntityJoin = root
                    .join("markets");
            Predicate predicate = cb.disjunction();;
            if(marketNames.contains(",")){
                List<String> marketList = Arrays.asList(marketNames.split(","));
                for(String market : marketList){
                    predicate = cb.or(predicate, cb.like(cb.lower(marketEntityJoin
                            .get("marketName")), "%" + market.toLowerCase() + "%"));
                }
            }
            else{
                predicate = cb.like(cb.lower(marketEntityJoin.get("marketName")), "%" + marketNames.toLowerCase() + "%");
            }
            return predicate;
        };
    }

    /**
     * Build the query based on the regions
     * @param regions regions
     * @return specification
     */
    public Specification<ApplicationEntity> hasRegion(String regions) {
        return (root, query, cb) -> {
            if (regions == null || regions.isEmpty()) {
                return cb.conjunction();
            }
            Join<ApplicationEntity, MarketEntity> marketEntityJoin = root
                    .join("markets");
            Predicate predicate = cb.disjunction();
            if(regions.contains(",")){
                List<String> regionList = Arrays.asList(regions.split(","));
                for(String region : regionList){
                    predicate = cb.or(predicate, cb.like(cb.lower(marketEntityJoin
                            .get("region")), "%" + region.toLowerCase() + "%"));
                }
            }
            else{
                predicate = cb.like(cb.lower(marketEntityJoin.get("region")), "%" + regions.toLowerCase() + "%");
            }
            return predicate;
        };
    }

    /**
     * Build the query based on the countries
     * @param countries countries
     * @return specification
     */
    public Specification<ApplicationEntity> hasCountry(String countries) {
        return (root, query, cb) -> {
            if (countries == null || countries.isEmpty()) {
                return cb.conjunction();
            }
            Join<ApplicationEntity, MarketEntity> marketEntityJoin = root
                    .join("markets");
            Predicate predicate = cb.disjunction();;
            if(countries.contains(",")){
                List<String> countryList = Arrays.asList(countries.split(","));
                for(String country : countryList){
                    predicate = cb.or(predicate, cb.like(cb.lower(marketEntityJoin
                            .get("country")), "%" + country.toLowerCase() + "%"));
                }
            }
            else{
                predicate = cb.like(cb.lower(marketEntityJoin.get("country")), "%" + countries.toLowerCase() + "%");
            }
            return predicate;
        };
    }

    /**
     * Build the query based on the sub-regions
     * @param subRegions sub-regions
     * @return specification
     */
    public Specification<ApplicationEntity> hasSubRegion(String subRegions) {
        return (root, query, cb) -> {
            if (subRegions == null || subRegions.isEmpty()) {
                return cb.conjunction();
            }
            Join<ApplicationEntity, MarketEntity> marketEntityJoin = root
                    .join("markets");
            Predicate predicate = cb.disjunction();;
            if(subRegions.contains(",")){
                List<String> subRegionList = Arrays.asList(subRegions.split(","));
                for(String subRegion : subRegionList){
                    predicate = cb.or(predicate, cb.like(cb.lower(marketEntityJoin
                            .get("subRegion")), "%" + subRegion.toLowerCase() + "%"));
                }
            }
            else {
                predicate = cb.like(cb.lower(marketEntityJoin.get("subRegion")), "%" + subRegions.toLowerCase() + "%");
            }
            return predicate;
        };
    }

    /**
     * Build the query based on the languages
     * @param languages languages
     * @return specification
     */
    public Specification<ApplicationEntity> hasLanguage(String languages) {
        return (root, query, cb) -> {
            if (languages == null || languages.isEmpty()) {
                return cb.conjunction();
            }
            Join<ApplicationEntity, LanguageEntity> languageEntityJoin = root
                    .join("languages");
            Predicate predicate = cb.disjunction();
            if(languages.contains(",")){
                List<String> languageList = Arrays.asList(languages.split(","));
                for(String language : languageList){
                    predicate = cb.or(predicate, cb.like(cb.lower(languageEntityJoin
                            .get("description")), "%" + language.toLowerCase() + "%"));
                }
            }
            else {
                predicate = cb.like(cb.lower(languageEntityJoin.get("description")), "%" + languages.toLowerCase() + "%");
            }
            return predicate;
        };
    }

    /**
     * Build the query based on the SSO enabled
     * @param ssoEnabled SSO enabled
     * @return specification
     */
    public Specification<ApplicationEntity> hasSSOEnabled(String ssoEnabled) {
        return (root, query, cb) -> {
            if (ssoEnabled == null || ssoEnabled.isEmpty()) {
                return cb.conjunction();
            }
            List<String> ssoEnabledList = Arrays.asList(ssoEnabled.split(","));
            Predicate predicate = cb.disjunction();
            for (String enabled : ssoEnabledList) {
                boolean isSSOEnabled = Boolean.parseBoolean(enabled);
                predicate = cb.or(predicate, cb.equal(root.get("isSSOEnabled"), isSSOEnabled));
            }
            return predicate;
        };
    }

    /**
     * Build the query based on the specialities
     * @param specialities specialities
     * @return specification
     */
    public Specification<ApplicationEntity> hasSpeciality(String specialities) {
        return (root, query, cb) -> {
            if (specialities == null || specialities.isEmpty()) {
                return cb.conjunction();
            }
            Join<ApplicationEntity, SpecialityEntity> specialityEntityJoin = root
                    .join("specialities");
            Predicate predicate = cb.disjunction();
            if(specialities.contains(",")){
                List<String> specialitiesList = Arrays.asList(specialities.split(","));
                for(String speciality : specialitiesList){
                    predicate = cb.or(predicate, cb.like(cb.lower(specialityEntityJoin
                            .get("specialityName")), "%" + speciality.toLowerCase() + "%"));
                }
            }
            else {
                predicate = cb.like(cb.lower(specialityEntityJoin.get("specialityName")), "%" + specialities.toLowerCase() + "%");
            }
            return predicate;
        };
    }

    /**
     * Build the query based on the modalities
     * @param modalities modalities
     * @return specification
     */
    public Specification<ApplicationEntity> hasModality(String modalities) {
        return (root, query, cb) -> {
            if (modalities == null || modalities.isEmpty()) {
                return cb.conjunction();
            }
            Join<ApplicationEntity, ModalityEntity> modalityEntityJoin = root
                    .join("modalities");
            Predicate predicate = cb.disjunction();
            if(modalities.contains(",")){
                List<String> modalitiesList = Arrays.asList(modalities.split(","));
                for(String modality : modalitiesList){
                    predicate = cb.or(predicate, cb.like(cb.lower(modalityEntityJoin
                            .get("modalityName")), "%" + modality.toLowerCase() + "%"));
                }
            }
            else {
                predicate = cb.like(cb.lower(modalityEntityJoin.get("modalityName")), "%" + modalities.toLowerCase() + "%");
            }
            return predicate;
        };
    }

    /**
     * Build the query based on the categories
     * @param categories categories
     * @return specification
     */
    public Specification<ApplicationEntity> hasCategory(String categories) {
        return (root, query, cb) -> {
            if (categories == null || categories.isEmpty()) {
                return cb.conjunction();
            }
            Join<ApplicationEntity, CategoryEntity> categoryEntityJoin = root
                    .join("category");
            Predicate predicate = cb.disjunction();
            if(categories.contains(",")){
                List<String> categoryList = Arrays.asList(categories.split(","));
                for(String category : categoryList){
                    predicate = cb.or(predicate, cb.like(cb.lower(categoryEntityJoin
                            .get("name")), "%" + category.toLowerCase() + "%"));
                }
            }
            else {
                predicate = cb.like(cb.lower(categoryEntityJoin.get("name")), "%" + categories.toLowerCase() + "%");
            }
            return predicate;
        };
    }
    /**
     * Build the query based on the application status
     * @param status status
     * @return specification
     */
    public Specification<ApplicationEntity> hasApplicationStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isEmpty()) {
                return cb.conjunction();
            }
            Join<ApplicationEntity, ApplicationStatusEntity> applicationStatusEntityJoin = root
                    .join("status");
            Predicate predicate = cb.disjunction();
            if(status.contains(",")){
                List<String> statusList = Arrays.asList(status.split(","));
                for(String statusName : statusList){
                    predicate = cb.or(predicate, cb.like(cb.lower(applicationStatusEntityJoin
                            .get("name")), "%" + statusName.toLowerCase() + "%"));
                }
            }
            else {
                predicate = cb.like(cb.lower(applicationStatusEntityJoin.get("name")), "%" + status.toLowerCase() + "%");
            }
            return predicate;
        };
    }

}
