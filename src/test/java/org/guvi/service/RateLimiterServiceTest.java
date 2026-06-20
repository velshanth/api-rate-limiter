package org.guvi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateLimiterServiceTest {

    private RateLimiterService rateLimiterService;

    @BeforeEach
    void setUp() {
        rateLimiterService = new RateLimiterService();
    }

    @Test
    void allowRequest_ShouldAllowFirstRequest() {

        boolean allowed =
                rateLimiterService.allowRequest("192.168.1.1");

        assertTrue(allowed);
    }

    @Test
    void allowRequest_ShouldAllowTenRequests() {

        String ip = "192.168.1.1";

        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimiterService.allowRequest(ip));
        }
    }

    @Test
    void allowRequest_ShouldBlockEleventhRequest() {

        String ip = "192.168.1.1";

        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimiterService.allowRequest(ip));
        }

        assertFalse(rateLimiterService.allowRequest(ip));
    }

    @Test
    void allowRequest_ShouldMaintainSeparateBucketsPerIp() {

        String ip1 = "192.168.1.1";
        String ip2 = "192.168.1.2";

        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimiterService.allowRequest(ip1));
        }

        assertFalse(rateLimiterService.allowRequest(ip1));

        assertTrue(rateLimiterService.allowRequest(ip2));
    }
}