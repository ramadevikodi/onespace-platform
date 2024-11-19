/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: GuavaCacheUtil.java
 */

package com.philips.onespace.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class GuavaCacheUtil<T> {

    private final Cache<String, T> guavaCache;

    @Autowired
    public GuavaCacheUtil(Duration expiryDuration) {
        this.guavaCache = CacheBuilder.newBuilder().expireAfterWrite(expiryDuration).build();
    }

    public GuavaCacheUtil(GuavaCacheUtil<T> cacheUtil) {
        this.guavaCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
        if (cacheUtil != null && cacheUtil.guavaCache != null) {
            cacheUtil.guavaCache.asMap().forEach(this.guavaCache::put);
        }
    }

    public void add(String key, T value) {
        guavaCache.put(key, value);
    }

    public T get(String key) {
        return guavaCache.getIfPresent(key);
    }

    public void invalidate(String key) {
        guavaCache.invalidate(key);
    }
}
