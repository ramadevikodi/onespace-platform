package com.philips.onespace.appdiscoveryframework.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.philips.onespace.service.StorageService;

class IconControllerTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private IconController iconController;

    @Value("${aws.icon bucketName}")
    private String iconBucketName;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    String name = "file.txt";
    String originalFileName = "file.txt";
    String contentType = "text/plain";
    byte[] content = new byte[10];

    @Test
    void testUpload() throws IOException {
        MultipartFile file = new MockMultipartFile(name, originalFileName, contentType, content);
        when(storageService.uploadFile(file, iconBucketName, name)).thenReturn(Boolean.TRUE);
        ResponseEntity<Boolean> response = iconController.upload(name, file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
        verify(storageService, times(1)).uploadFile(file, iconBucketName, name);
    }

    @Test
    void testUploadFailure() throws IOException {

        MultipartFile file = new MockMultipartFile(name, originalFileName, contentType, content);
        when(storageService.uploadFile(file, iconBucketName, name)).thenReturn(Boolean.FALSE);
        ResponseEntity<Boolean> response = iconController.upload(name, file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
        verify(storageService, times(1)).uploadFile(file, iconBucketName, name);
    }

    @Test
    void testUpdate_success() throws IOException {

        MultipartFile file = new MockMultipartFile(name, originalFileName, contentType, content);
        when(storageService.uploadFile(file, "iconBucketName", name)).thenReturn(Boolean.TRUE);
        ResponseEntity<Boolean> response = iconController.update(name, file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
        verify(storageService, times(1)).uploadFile(file,null, name);
    }

    @Test
    void testUpdate_failure() throws IOException {

        MultipartFile file = new MockMultipartFile(name, originalFileName, contentType, content);
        when(storageService.uploadFile(file, "iconBucketName", name)).thenReturn(Boolean.FALSE);
        ResponseEntity<Boolean> response = iconController.update(name, file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
        verify(storageService, times(1)).uploadFile(file, null, name);
    }
}
