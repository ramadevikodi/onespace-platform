package com.philips.onespace.appdiscoveryframework.controller;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.philips.onespace.appdiscoveryframework.service.CategoryService;
import com.philips.onespace.appdiscoveryframework.service.RateLimitService;
import com.philips.onespace.dto.Value;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


@Nested
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private RateLimitService rateLimitService;

    @InjectMocks
    private CategoryController categoryController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listCategories_whenRateLimitAllows_shouldReturnOk() throws BadRequestException {

        List<Value> categories = new ArrayList<>();
        categories.add(new Value());
        when(categoryService.getCategories()).thenReturn(categories);
        when(rateLimitService.consume()).thenReturn(Boolean.TRUE);


        ResponseEntity<List<Value>> response = categoryController.listCategories();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
        verify(rateLimitService, times(1)).consume();
        verify(categoryService, times(1)).getCategories();
    }

    @Test
    void listCategories_whenRateLimitExceeded_shouldReturnTooManyRequests() throws BadRequestException {

        when(rateLimitService.consume()).thenReturn(Boolean.FALSE);
        ResponseEntity<List<Value>> response = categoryController.listCategories();
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertNull(response.getBody());
        verify(rateLimitService, times(1)).consume();

    }
}

