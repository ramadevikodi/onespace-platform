package com.philips.onespace.appdiscoveryframework.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.philips.onespace.appdiscoveryframework.mapper.UserMarketMapMapper;
import com.philips.onespace.dto.UserMarketMap;
import com.philips.onespace.jpa.entity.UserMarketMapEntity;
import com.philips.onespace.jpa.repository.UserMarketMappingRepository;


@ExtendWith(MockitoExtension.class)
class UserMarketMappingServiceImplTest {

    @Mock
    private UserMarketMappingRepository userMarketMappingRepository;

    @Mock
    private UserMarketMapMapper userMarketMapMapper;

    @InjectMocks
    private UserMarketMappingServiceImpl userMarketMappingService;

    @Test
    void test_get_markets_associated_to_user()  {
        UUID hspIamUserUuid = UUID.fromString("0663454b-685a-4583-936e-98263e381666");
        UUID marketId = UUID.fromString("e37245fa-b389-4429-aaae-ac64cd989f77");
        List<UserMarketMapEntity> userMarketMapEntities = new ArrayList<>();
        userMarketMapEntities.add(UserMarketMapEntity.builder()
                .marketId(marketId)
                .hspUserUuid(hspIamUserUuid)
                .build());
        when(userMarketMappingRepository.findAllByHspUserUuid(hspIamUserUuid)).thenReturn(userMarketMapEntities);
        List<UserMarketMap> userMarketMaps = userMarketMappingService.getMarketsAssociatedToUser(hspIamUserUuid);
        assertNotNull(userMarketMapMapper);
        assertNotNull(hspIamUserUuid);
        assertNotNull(userMarketMaps);
    }
    @Test
    void test_get_markets_associated_to_user_no_markets() {
        UUID hspIamUserUuid = UUID.fromString("0663454b-685a-4583-936e-98263e381666");
        when(userMarketMappingRepository.findAllByHspUserUuid(hspIamUserUuid)).thenReturn(new ArrayList<>());
        List<UserMarketMap> userMarketMaps = userMarketMappingService.getMarketsAssociatedToUser(hspIamUserUuid);
        assertNotNull(userMarketMaps);
        assertTrue(userMarketMaps.isEmpty());
    }

    @Test
    void test_get_markets_associated_to_user_null_uuid() {
        UUID hspIamUserUuid = null;
        List<UserMarketMap> userMarketMaps = userMarketMappingService.getMarketsAssociatedToUser(hspIamUserUuid);
        assertNotNull(userMarketMaps);
        assertTrue(userMarketMaps.isEmpty());
    }

    @Test
    void test_get_markets_associated_to_user_repository_returns_null() {
        UUID hspIamUserUuid = UUID.fromString("0663454b-685a-4583-936e-98263e381666");
        when(userMarketMappingRepository.findAllByHspUserUuid(hspIamUserUuid)).thenReturn(null);
        List<UserMarketMap> userMarketMaps = userMarketMappingService.getMarketsAssociatedToUser(hspIamUserUuid);
        assertNotNull(userMarketMaps);
        assertTrue(userMarketMaps.isEmpty());
    }

}
