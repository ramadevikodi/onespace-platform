package com.philips.onespace.appdiscoveryframework.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Service
public class RateLimitService {

    private final Bucket bucket;

    public RateLimitService(@Value("${rateLimit.capacity}") int capacity, @Value("${rateLimit.refillTokens}") int refillTokens, @Value("${rateLimit.refillPeriod}") int refillPeriod) {

        Bandwidth limit = Bandwidth.classic(capacity, Refill.greedy(refillTokens, Duration.ofMinutes(refillPeriod)));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    public boolean consume() {
        return bucket.tryConsume(1);
    }

}
