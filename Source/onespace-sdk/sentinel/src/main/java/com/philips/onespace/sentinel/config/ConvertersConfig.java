package com.philips.onespace.sentinel.config;

import com.philips.onespace.sentinel.converter.SentinelJsonStrToObjectConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConvertersConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SentinelJsonStrToObjectConverter());
    }
}