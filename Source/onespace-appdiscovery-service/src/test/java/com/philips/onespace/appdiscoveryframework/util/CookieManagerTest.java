package com.philips.onespace.appdiscoveryframework.util;

import com.philips.onespace.util.EncryptionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CookieManagerTest {

    @Mock
    private EncryptionUtil encryptionUtil;

    @InjectMocks
    private CookieManager cookieManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static final String COOKIE_NAME = "SESSION_COOKIE";
    private static final String COOKIE_VALUE = "testValue";
    private static final String ENCRYPTED_VALUE = "encryptedValue";
    private static final Long EXPIRY_IN_SECONDS = 3600L;
    private static final String SESSION_COOKIE = "dummySessionCookie";
    private static final String DECRYPTED_COOKIE = "accessToken;refreshToken";

    @Test
    public void createCookie_withNonEmptyValue_shouldReturnEncryptedCookie(){

        when(encryptionUtil.encrypt(COOKIE_VALUE)).thenReturn(ENCRYPTED_VALUE);
        ResponseCookie responseCookie = cookieManager.createCookie(COOKIE_NAME, COOKIE_VALUE, EXPIRY_IN_SECONDS);

        assertNotNull(responseCookie);
        assertEquals(COOKIE_NAME, responseCookie.getName());
        assertEquals(ENCRYPTED_VALUE, responseCookie.getValue());
        assertTrue(responseCookie.isSecure());
        assertEquals(Cookie.SameSite.NONE.attributeValue(), responseCookie.getSameSite());
        assertEquals("/", responseCookie.getPath());
        assertEquals(EXPIRY_IN_SECONDS, responseCookie.getMaxAge().getSeconds());

        verify(encryptionUtil, times(1)).encrypt(COOKIE_VALUE);
    }

    @Test
    public void createCookie_withEmptyValue_shouldReturnNonEncryptedCookie(){

        String emptyValue = "";
        ResponseCookie responseCookie = cookieManager.createCookie(COOKIE_NAME, emptyValue, EXPIRY_IN_SECONDS);
        assertNotNull(responseCookie);
        assertEquals(COOKIE_NAME, responseCookie.getName());
        assertEquals(emptyValue, responseCookie.getValue());
        assertTrue(responseCookie.isSecure());
        assertEquals(Cookie.SameSite.NONE.attributeValue(), responseCookie.getSameSite());
        assertEquals("/", responseCookie.getPath());
        assertEquals(EXPIRY_IN_SECONDS, responseCookie.getMaxAge().getSeconds());
        verify(encryptionUtil, never()).encrypt(anyString());
    }

    @Test
    public void createCookie_withNullValue_shouldReturnNonEncryptedCookie(){

        ResponseCookie responseCookie = cookieManager.createCookie(COOKIE_NAME, null, EXPIRY_IN_SECONDS);
        assertNotNull(responseCookie);
        assertEquals(COOKIE_NAME, responseCookie.getName());
        assertNull(null, responseCookie.getValue());
        assertTrue(responseCookie.isSecure());
        assertEquals(Cookie.SameSite.NONE.attributeValue(), responseCookie.getSameSite());
        assertEquals("/", responseCookie.getPath());
        assertEquals(EXPIRY_IN_SECONDS, responseCookie.getMaxAge().getSeconds());
        verify(encryptionUtil, never()).encrypt(anyString());
    }
    @Test
    public void testGetAccessToken(){

        when(encryptionUtil.decrypt(SESSION_COOKIE)).thenReturn(DECRYPTED_COOKIE);
        String accessToken = cookieManager.getAccessToken(SESSION_COOKIE);
        assertEquals("accessToken", accessToken);
        verify(encryptionUtil, times(1)).decrypt(SESSION_COOKIE);

    }
    @Test
    public void testGetRefreshToken(){

        when(encryptionUtil.decrypt(SESSION_COOKIE)).thenReturn(DECRYPTED_COOKIE);
        String refreshToken = cookieManager.getRefreshToken(SESSION_COOKIE);
        assertEquals("refreshToken", refreshToken);
        verify(encryptionUtil, times(1)).decrypt(SESSION_COOKIE);

    }
    @Test
    public void testDecryptAndSplitSessionCookie(){

        when(encryptionUtil.decrypt(SESSION_COOKIE)).thenReturn(DECRYPTED_COOKIE);
        String[] result = cookieManager.decryptAndSplitSessionCookie(SESSION_COOKIE);
        assertArrayEquals(new String[]{"accessToken", "refreshToken"}, result);
        verify(encryptionUtil, times(1)).decrypt(SESSION_COOKIE);
    }
}
