package com.philips.onespace.cache;

import com.philips.onespace.model.Group;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class CacheConfig {

    @Bean
    public Duration defaultExpiryDuration() {
        return Duration.ofSeconds(1799);
    }

    @Bean
    public GuavaCacheUtil<String> iamTokenCache(Duration defaultExpiryDuration) {
        return new GuavaCacheUtil<>(defaultExpiryDuration);
    }

    @Bean
    public GuavaCacheUtil<String> iamUserCache() {
        return new GuavaCacheUtil<>(Duration.ofHours(12));
    }

    @Bean
    public GuavaCacheUtil<List<Group>> groupCache() {
        return new GuavaCacheUtil<>(Duration.ofHours(24));
    }
}

