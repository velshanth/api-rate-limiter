package org.guvi.service;

import org.guvi.model.RateLimitBucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<String, RateLimitBucket> buckets =
            new ConcurrentHashMap<>();

    private static final int CAPACITY = 10;

    private static final long REFILL_INTERVAL = 60000;

    public synchronized boolean allowRequest(String clientIp) {

        long now = System.currentTimeMillis();

        buckets.putIfAbsent(
                clientIp,
                new RateLimitBucket(CAPACITY, now)
        );

        RateLimitBucket bucket = buckets.get(clientIp);

        long elapsedTime =
                now - bucket.getLastRefillTimeStamp();

        if (elapsedTime > REFILL_INTERVAL) {

            bucket.setTokens(CAPACITY);

            bucket.setLastRefillTimeStamp(now);
        }

        if (bucket.getTokens() > 0) {

            bucket.setTokens(bucket.getTokens() - 1);

            return true;
        }

        return false;
    }
}
