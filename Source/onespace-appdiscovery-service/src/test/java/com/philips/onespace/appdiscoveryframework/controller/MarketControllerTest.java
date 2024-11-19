package com.philips.onespace.appdiscoveryframework.controller;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.philips.onespace.appdiscoveryframework.service.MarketService;
import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.dto.Market;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

public class MarketControllerTest {

    @InjectMocks
    private MarketController marketController;

    @Mock
    private RateLimitService rateLimitService;

    @Mock
    private MarketService marketService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListMarketSuccess() throws BadRequestException{

        List<Market> mockMarkets = Arrays.asList(new Market(), new Market());
        when(marketService.getMarket()).thenReturn(mockMarkets);
        when(rateLimitService.consume()).thenReturn(Boolean.TRUE);


        ResponseEntity<List<Market>> response = marketController.listMarket();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMarkets, response.getBody());
    }

    @Test
    public void testListMarketTooManyRequests()throws BadRequestException {

        when(rateLimitService.consume()).thenReturn(Boolean.FALSE);
        ResponseEntity<List<Market>> response = marketController.listMarket();
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertNull(response.getBody());
    }
}
