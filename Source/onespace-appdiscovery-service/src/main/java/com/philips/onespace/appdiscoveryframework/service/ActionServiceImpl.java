/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ActionServiceImpl.java
 */

package com.philips.onespace.appdiscoveryframework.service;

import com.philips.onespace.appdiscoveryframework.mapper.ActionMapper;
import com.philips.onespace.appdiscoveryframework.service.interfaces.ActionService;
import com.philips.onespace.appdiscoveryframework.util.SecurityContextUtil;
import com.philips.onespace.cache.GuavaCacheUtil;
import com.philips.onespace.dto.Action;
import com.philips.onespace.dto.ActionId;
import com.philips.onespace.jpa.entity.ActionEntity;
import com.philips.onespace.jpa.entity.ActionOwnersEntity;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.repository.ActionOwnersRepository;
import com.philips.onespace.jpa.repository.ActionRepository;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.model.AppNotificationEvent;
import com.philips.onespace.model.Users;
import com.philips.onespace.service.IamService;
import com.philips.onespace.util.ErrorMessages;
import com.philips.onespace.util.EventType;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.philips.onespace.dto.ActionStatus.EXPIRED;
import static com.philips.onespace.dto.ActionStatus.TODO;
import static com.philips.onespace.logging.LoggingAspect.logData;
import static com.philips.onespace.util.DateUtil.formatDateTime;
import static com.philips.onespace.util.ErrorMessages.ERR_SOURCE_APPLICATION_DOESNOT_EXIST;
import static com.philips.onespace.util.ErrorMessages.MISSING_ACTION_UPDATE_PARAM_ERR_CODE;

@Service
public class ActionServiceImpl implements ActionService {


    private final ActionRepository actionRepository;


    private final ActionOwnersRepository actionOwnersRepository;

    private final ApplicationRepository applicationRepository;

    private final ActionMapper actionMapper;

    private final SecurityContextUtil securityContextUtil;


    private final ApplicationEventPublisher applicationEventPublisher;


    private final IamService iamService;

    private final GuavaCacheUtil<String> iamUserCache;

    public ActionServiceImpl(GuavaCacheUtil<String> iamUserCache, ApplicationRepository applicationRepository,
                             IamService iamService, SecurityContextUtil securityContextUtil, ActionMapper actionMapper,
    ActionRepository actionRepository, ActionOwnersRepository actionOwnersRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.iamUserCache = new GuavaCacheUtil<>(iamUserCache);
        this.applicationRepository = applicationRepository;
        this.iamService = iamService;
        this.securityContextUtil = securityContextUtil;
        this.actionMapper = actionMapper;
        this.actionOwnersRepository = actionOwnersRepository;
        this.actionRepository = actionRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
   	 * This method saves action.
   	 *
   	 * @param action, the action
   	 * @return ActionId, the action id
   	 */
    @Override
    public ActionId createAction(Action action) throws BadRequestException {

        ApplicationEntity applicationEntity = validateSource(action);

        validateInitiatorAndPotentialOwner(action);
        validateAndSetDefaultValues(action);
        validateDateFields(action);

        ActionEntity actionEntity = actionMapper.dtoToEntity(action);
        actionEntity.setDateTime(formatDateTime(action.getDateTime()));
        actionEntity.setDueDateTime(action.getDueDateTime() != null ? formatDateTime(action.getDueDateTime()) : null);
        actionEntity.setExpiryDateTime(action.getExpiryDateTime() != null ? formatDateTime(action.getExpiryDateTime()) : null);
        actionEntity.setApplication(applicationEntity);

        List<ActionOwnersEntity> actionOwnersEntities = actionMapper.mapActionOwnersEntity(action.getPotentialOwner(), action.getStatus(), actionEntity);
        actionEntity.setActionOwnersEntities(actionOwnersEntities);

        ActionId actionId = ActionId.builder().id(actionRepository.save(actionEntity).getId()).build();

        if (Boolean.TRUE.equals(action.getNotify())) {
            applicationEventPublisher.publishEvent(AppNotificationEvent.builder().eventType(EventType.ACTION_INITIATED).actionId(actionId.getId()).applicationId(UUID.fromString(action.getSource().getId())).build());
            logData("Notification initiated for action  ", actionId.getId());
        }

        return actionId;
    }

    /**
	 * This method updates action.
	 *
	 * @param action the action
	 * @return update action status
	 */
    @Override
    public int updateStatus(List<Action> actionList) throws BadRequestException {
        validateMandatoryParametersForUpdate(actionList);
        int totalUpdatedRows = 0;
        for (Action action : actionList) {
            if (StringUtils.isEmpty(action.getCompletedAtDateTime())) {
                action.setCompletedAtDateTime(formatDateTime(LocalDateTime.now()));
            }
            if (!(action.getPotentialOwner().get(0).toString().equals(getLoggedUser()))) {
                throw new BadRequestException(ErrorMessages.INVALID_POTENTIAL_OWNER);
            }
            int updatedRows = actionOwnersRepository.updateByActionIdAndOwner(action.getStatus(), formatDateTime(action.getCompletedAtDateTime()), action.getId(), action.getPotentialOwner().get(0));
            totalUpdatedRows += updatedRows;
        }
        return totalUpdatedRows;
    }

    /**
	 * This method retrieves all the actions.
	 *
	 * @return List of actions
	 */
    @Override
    public List<Action> getActions() {
        UUID loggedInUserId = UUID.fromString(getLoggedUser());
        List<ActionEntity> actionEntities = actionRepository.findByPotentialOwner(loggedInUserId);
        return actionEntities.stream().map(actionEntity -> actionMapper.entityToDto(actionEntity, loggedInUserId)).toList();
    }

    /**
	 * This method marks the expired actions.
	 *
	 * @return expired action status
	 */
    public int markExpiredActions() {
        int updatedRows = 0;
        List<ActionOwnersEntity> expiredActions = actionOwnersRepository.findExpiredActions();
        for (ActionOwnersEntity actionOwnersEntity : expiredActions) {
            updatedRows = actionOwnersRepository.markAsExpired(actionOwnersEntity.getId(), EXPIRED.toString());
        }
        return updatedRows;
    }

    private void validateMandatoryParametersForUpdate(List<Action> actionList) throws BadRequestException {
        for (Action action : actionList) {
            if (action.getStatus() == null || action.getId() == null || action.getPotentialOwner().isEmpty()) {
                throw new BadRequestException(MISSING_ACTION_UPDATE_PARAM_ERR_CODE);
            }
        }

    }

    private String getLoggedUser() {
        return securityContextUtil.getUserIdFromPrincipal(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    private void validateDateFields(Action action) throws BadRequestException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (!formatDateTime(action.getDateTime()).format(formatter).equalsIgnoreCase(LocalDateTime.now().format(formatter))) {
            throw new BadRequestException(ErrorMessages.INVALID_DATE_TIME);
        }
        if (StringUtils.isNotEmpty(action.getDueDateTime()) && formatDateTime(action.getDueDateTime()).isBefore(LocalDateTime.now())) {
            throw new BadRequestException(ErrorMessages.INVALID_DUE_DATE);
        }
        if (StringUtils.isNotEmpty(action.getExpiryDateTime()) && formatDateTime(action.getExpiryDateTime()).isBefore(LocalDateTime.now())) {
            throw new BadRequestException(ErrorMessages.INVALID_EXPIRY_DATE);
        }
    }

    private void validateAndSetDefaultValues(Action action) throws BadRequestException {
        if (StringUtils.isEmpty(action.getStatus())) action.setStatus(TODO.toString());

        if (action.getDateTime() == null) action.setDateTime(formatDateTime(LocalDateTime.now()));

        if (!action.getStatus().equalsIgnoreCase(TODO.toString()))
            throw new BadRequestException(ErrorMessages.NOT_APPLICABLE_STATUS);

        if (!(action.getInitiator().toString().equals(getLoggedUser()))) {
            throw new BadRequestException(ErrorMessages.INVALID_INITIATOR);
        }
    }

    private void validateInitiatorAndPotentialOwner(Action action) throws BadRequestException {
        String iamToken = iamService.getIAMToken();

        if (!isUserValid(action.getInitiator().toString(), iamToken)) {
            throw new BadRequestException(ErrorMessages.INVALID_IAM_USER);
        }

        for (UUID owner : action.getPotentialOwner()) {
            if (!isUserValid(owner.toString(), iamToken)) {
                throw new BadRequestException(ErrorMessages.INVALID_IAM_USER);
            }
        }
    }

    private boolean isUserValid(String userId, String iamToken) {

        if (iamUserCache.get(userId) != null) {
            return true;
        }

        Users user = iamService.getUsers(userId, iamToken);
        if (user.getResources().isEmpty()) {
            return false;
        }

        iamUserCache.add(userId, userId);
        return true;
    }

    private ApplicationEntity validateSource(Action action) throws BadRequestException {
        Optional<ApplicationEntity> applicationEntityOpt = applicationRepository.findById(UUID.fromString(action.getSource().getId()));
        if (applicationEntityOpt.isEmpty()) {
            throw new BadRequestException(ERR_SOURCE_APPLICATION_DOESNOT_EXIST);
        }
        return applicationEntityOpt.get();
    }
}
