package com.philips.onespace.appdiscoveryframework.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component("commonTestMethod")
public class CommonTestMethod {

/**
     * mockMvcResult() method returns mvcResult.
     *
     * @param urlTemplate   - test URL
     * @return - Provides access to the result of an executed request
     * @throws Exception To handle exceptions
     */

    public static MvcResult mockMvcResult(MockMvc mockMvc, String urlTemplate,
                                          MediaType contentType, String acceptType,
                                          String locale, String apiVersion) throws Exception {
        MvcResult mvcResult =
                mockMvc
                        .perform(
                                MockMvcRequestBuilders.get(urlTemplate)
                                        .contentType(contentType)
                                        .accept(acceptType)
                                        .header("accept-language", locale)
                                        .header("api-version", apiVersion))
                        .andReturn();
        return mvcResult;
    }


/**
     * assertStatusTwoHundred() checks if the returned status is 200.
     *
     * @param mvcResult - contains result object with status code
     */


    public static void assertStatusTwoHundred(MvcResult mvcResult) {
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }


/**
     * mockMvcAndExpect() checks if the returned status is 200.
     *
     * @param mockMvc - MockMvc instance
     * @throws Exception To handle exceptions
     */

    public static ResultActions mockMvcAndExpect(MockMvc mockMvc, String urlTemplate,
                                                 MediaType contentType, String acceptType,
                                                 String locale, String apiVersion) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(urlTemplate).accept(acceptType);
        if (contentType != null) {
            mockHttpServletRequestBuilder.contentType(contentType);
        }
        if (locale != null) {
            mockHttpServletRequestBuilder.header("accept-language", locale);
        }
        if (apiVersion != null) {
            mockHttpServletRequestBuilder.header("api-version", apiVersion);
        }
        return mockMvc.perform(mockHttpServletRequestBuilder);
    }
    public int getStatus(MvcResult mvcResult) {
        return mvcResult.getResponse().getStatus();
    }
    public String getContent(MvcResult mvcResult) throws UnsupportedEncodingException {
        return mvcResult.getResponse().getContentAsString();
    }


}

