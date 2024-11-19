package com.philips.onespace.appdiscoveryframework.service;

import com.philips.onespace.appdiscoveryframework.mapper.ActionMapper;
import com.philips.onespace.appdiscoveryframework.util.SecurityContextUtil;
import com.philips.onespace.cache.GuavaCacheUtil;
import com.philips.onespace.dto.Action;
import com.philips.onespace.dto.ActionId;
import com.philips.onespace.dto.Source;
import com.philips.onespace.jpa.entity.ActionEntity;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.repository.ActionOwnersRepository;
import com.philips.onespace.jpa.repository.ActionRepository;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.model.UserResource;
import com.philips.onespace.model.Users;
import com.philips.onespace.service.IamService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.philips.onespace.dto.ActionStatus.TODO;
import static com.philips.onespace.util.Constants.DATE_FORMAT_WITHOUT_TIMEZONE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ActionServiceImplTest {

    @InjectMocks
    private ActionServiceImpl actionService;

    private ActionRepository actionRepository;

    private ActionOwnersRepository actionOwnersRepository;

    private ApplicationRepository applicationRepository;

    private ActionMapper actionMapper;

    private SecurityContextUtil securityContextUtil;

    private IamService iamService;

    private List<UUID> potentialOwnerList;

    @BeforeEach
    void setUp() {
        GuavaCacheUtil<String> iamUserCache = new GuavaCacheUtil<>(Duration.ofHours(1));
        applicationRepository = Mockito.mock(ApplicationRepository.class);
        iamService = Mockito.mock(IamService.class);
        securityContextUtil = mock(SecurityContextUtil.class);
        actionMapper = mock(ActionMapper.class);
        actionRepository = mock(ActionRepository.class);
        actionOwnersRepository = mock(ActionOwnersRepository.class);
        ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
        actionService = new ActionServiceImpl(iamUserCache, applicationRepository, iamService, securityContextUtil, actionMapper, actionRepository, actionOwnersRepository, applicationEventPublisher);

        String loggedInUserId = "1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f";
        potentialOwnerList = Collections.singletonList(UUID.fromString(loggedInUserId));
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(loggedInUserId);
    }

    @Test
    void testCreateActionWithValidData() throws BadRequestException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_TIMEZONE);
        String strLocalDateTime = LocalDateTime.now().format(formatter);
        UserResource userResource = UserResource.builder().id("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f").userName("Application Integrator").build();

        Action action = new Action();
        action.setSource(Source.builder().id("8a1bf01b-d543-4aab-83a8-b466c6234f05").name("Dose Management").build());
        action.setInitiator(UUID.fromString("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f"));
        action.setPotentialOwner(potentialOwnerList);
        action.setNotify(Boolean.FALSE);
        action.setStatus(TODO.toString());
        action.setDateTime(strLocalDateTime);

        ApplicationEntity appEntity = new ApplicationEntity();
        appEntity.setId(UUID.fromString(action.getSource().getId()));

        String iamToken = "test-token";
        when(iamService.getIAMToken()).thenReturn(iamToken);

        Users mockedUser = new Users();
        mockedUser.setResources(Collections.singletonList(userResource)); // Add appropriate structure if needed

        when(iamService.getUsers("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f", iamToken)).thenReturn(mockedUser);
        when(applicationRepository.findById(any())).thenReturn(Optional.of(appEntity));
        when(actionMapper.dtoToEntity(action)).thenReturn(new ActionEntity());
        when(actionRepository.save(any())).thenReturn(new ActionEntity());
        when(securityContextUtil.getUserIdFromPrincipal(any())).thenReturn("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f");

        ActionId actionId = actionService.createAction(action);
        assertNotNull(actionId);
        verify(actionRepository, times(1)).save(any(ActionEntity.class));
    }

    @Test
    void testCreateActionWithMissingStatus() throws BadRequestException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_TIMEZONE);
        String strLocalDateTime = LocalDateTime.now().format(formatter);
        UserResource userResource = UserResource.builder().id("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f").userName("Application Integrator").build();

        Action action = new Action();
        action.setSource(Source.builder().id("8a1bf01b-d543-4aab-83a8-b466c6234f05").name("Dose Management").build());
        action.setInitiator(UUID.fromString("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f"));
        action.setPotentialOwner(potentialOwnerList);
        action.setDateTime(strLocalDateTime);

        ApplicationEntity appEntity = new ApplicationEntity();
        appEntity.setId(UUID.fromString(action.getSource().getId()));

        String iamToken = "test-token";
        when(iamService.getIAMToken()).thenReturn(iamToken);

        Users mockedUser = new Users();
        mockedUser.setResources(Collections.singletonList(userResource)); // Add appropriate structure if needed

        when(iamService.getUsers("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f", iamToken)).thenReturn(mockedUser);
        when(applicationRepository.findById(any())).thenReturn(Optional.of(appEntity));
        when(actionMapper.dtoToEntity(action)).thenReturn(new ActionEntity());
        when(actionRepository.save(any())).thenReturn(new ActionEntity());
        when(securityContextUtil.getUserIdFromPrincipal(any())).thenReturn("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f");

        ActionId actionId = actionService.createAction(action);
        assertNotNull(actionId);
        assertEquals(TODO.toString(), action.getStatus());
        verify(actionRepository, times(1)).save(any(ActionEntity.class));
    }

    @Test
    void testCreateActionWithInvalidStatus() {
        Action action = new Action();
        action.setSource(Source.builder().id("8a1bf01b-d543-4aab-83a8-b466c6234f05").name("Dose Management").build());
        action.setInitiator(UUID.fromString("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f"));
        action.setPotentialOwner(potentialOwnerList);
        action.setStatus("Invalid_status");
        assertThrows(BadRequestException.class, () -> actionService.createAction(action));
    }

    @Test
    void testCreateActionWithMissingDateTime() throws BadRequestException {

        UserResource userResource = UserResource.builder().id("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f").userName("Application Integrator").build();
        Action action = new Action();
        action.setSource(Source.builder().id("8a1bf01b-d543-4aab-83a8-b466c6234f05").name("Dose Management").build());
        action.setInitiator(UUID.fromString("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f"));
        action.setPotentialOwner(potentialOwnerList);

        ApplicationEntity appEntity = new ApplicationEntity();
        appEntity.setId(UUID.fromString(action.getSource().getId()));

        String iamToken = "test-token";
        when(iamService.getIAMToken()).thenReturn(iamToken);

        Users mockedUser = new Users();
        mockedUser.setResources(Collections.singletonList(userResource)); // Add appropriate structure if needed

        when(iamService.getUsers("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f", iamToken)).thenReturn(mockedUser);
        when(applicationRepository.findById(any())).thenReturn(Optional.of(appEntity));
        when(actionMapper.dtoToEntity(action)).thenReturn(new ActionEntity());
        when(actionRepository.save(any())).thenReturn(new ActionEntity());
        when(securityContextUtil.getUserIdFromPrincipal(any())).thenReturn("1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f");

        ActionId actionId = actionService.createAction(action);
        assertNotNull(actionId);
        assertNotNull(action.getDateTime());
        verify(actionRepository, times(1)).save(any(ActionEntity.class));
    }


    @Test
    void testUpdateStatusWithValidData() throws BadRequestException {
        List<Action> actionList = new ArrayList<>();
        Action action = new Action();
        action.setStatus(TODO.toString());
        action.setId(UUID.fromString(UUID.randomUUID().toString()));
        action.setPotentialOwner(potentialOwnerList);
        actionList.add(action);

        when(securityContextUtil.getUserIdFromPrincipal(any())).thenReturn(String.valueOf(action.getPotentialOwner().get(0)));
        when(actionOwnersRepository.updateByActionIdAndOwner(anyString(), any(), any(), any())).thenReturn(1);

        int updatedRows = actionService.updateStatus(actionList);
        assertEquals(1, updatedRows);
        verify(actionOwnersRepository, times(1)).updateByActionIdAndOwner(anyString(), any(), any(), any());
    }

    @Test
    void testUpdateStatusWithMissingMandatoryParameters() {
        List<Action> actionList = new ArrayList<>();
        Action action = new Action();
        actionList.add(action);
        assertThrows(BadRequestException.class, () -> actionService.updateStatus(actionList));
    }

    @Test
    void testGetActions() {
        UUID loggedInUserId = UUID.randomUUID();
        List<ActionEntity> actionEntities = new ArrayList<>();
        ActionEntity actionEntity = new ActionEntity();
        actionEntities.add(actionEntity);

        when(securityContextUtil.getUserIdFromPrincipal(any())).thenReturn(loggedInUserId.toString());
        when(actionRepository.findByPotentialOwner(any(UUID.class))).thenReturn(actionEntities);
        when(actionMapper.entityToDto(any(ActionEntity.class), any(UUID.class))).thenReturn(new Action());

        List<Action> actions = actionService.getActions();

        assertNotNull(actions);
        assertFalse(actions.isEmpty());
        verify(actionRepository, times(1)).findByPotentialOwner(any(UUID.class));
    }
}

