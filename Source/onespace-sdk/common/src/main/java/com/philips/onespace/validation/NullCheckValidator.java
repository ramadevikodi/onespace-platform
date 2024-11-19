/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: NullCheckValidator.java
 */

package com.philips.onespace.validation;

import static com.philips.onespace.util.Constants.AWAITING_BUSINESS_OWNER_APPROVAL;
import static com.philips.onespace.util.Constants.AWAITING_MARKET_SOLUTION_OWNER_APPROVAL;
import static com.philips.onespace.util.Constants.DEPLOYMENT;
import static com.philips.onespace.util.Constants.DRAFT;
import static com.philips.onespace.util.Constants.ID;
import static com.philips.onespace.util.Constants.OWNERSHIP;
import static com.philips.onespace.util.Constants.REGISTRATION;
import static com.philips.onespace.util.Constants.STATUS;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import com.philips.onespace.jpa.entity.ApplicationStatusEntity;
import com.philips.onespace.jpa.repository.ApplicationStatusRepository;
import com.philips.onespace.validator.NotNull;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

@Component
public class NullCheckValidator implements ConstraintValidator<NotNull, Object> {
    private final ApplicationStatusRepository applicationStatusRepository;
    private String[] draftRequiredFields;
    private String[] l1ApprovalFields;
    private String[] deploymentFields;
    private  String[] ownershipFields;
    private boolean valid;

    public NullCheckValidator(ApplicationStatusRepository applicationStatusRepository) {
        this.applicationStatusRepository = applicationStatusRepository;
    }

    @Override
    public void initialize(final NotNull constraintAnnotation) {
        draftRequiredFields = new String[]{"name", "shortDescription", "version", "businessUnit.id"};
        l1ApprovalFields = new String[] {"specialities", "modalities",
                "languages", "category.id"};
        deploymentFields = new String[] {"url"};
        ownershipFields = new String[] {"costCenter"};
    }

    @SneakyThrows
    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        valid = true;
        Object statusId = BeanUtils.getProperty(value, STATUS+"."+ID);
        Optional<ApplicationStatusEntity> statusName = applicationStatusRepository.
                findById(UUID.fromString(statusId.toString()));
        if(statusName.get().getName().equals(DRAFT)){
            isValueEmpty(value, context, draftRequiredFields, REGISTRATION);
        }
        else if (statusName.get().getName().equals(AWAITING_BUSINESS_OWNER_APPROVAL)) {
            isValueEmpty(value, context, draftRequiredFields, REGISTRATION);
            isValueEmpty(value, context, l1ApprovalFields, REGISTRATION);
            isValueEmpty(value, context, deploymentFields, DEPLOYMENT);
        } else if (statusName.get().getName().equals(AWAITING_MARKET_SOLUTION_OWNER_APPROVAL)) {
            isValueEmpty(value, context, ownershipFields, OWNERSHIP);
        }
        return valid;
    }

    private void isValueEmpty(Object value, ConstraintValidatorContext context, String[] requiredFields, String objectDto)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (String fieldName : requiredFields) {
                final Object fieldValue = BeanUtils.getProperty(value, objectDto + "." + fieldName);
                if (fieldValue == null || fieldValue.toString().trim().isEmpty()) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("empty_" + fieldName)
                            .addPropertyNode(objectDto + "." + fieldName)
                            .addConstraintViolation();
                    valid = false;
                }
        }
    }
}