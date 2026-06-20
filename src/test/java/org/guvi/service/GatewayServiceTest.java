package org.guvi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GatewayServiceTest {

    private GatewayService gatewayService;

    @BeforeEach
    void setUp() {
        gatewayService = new GatewayService();
    }

    @Test
    void serviceA_ShouldReturnExpectedResponse() {
        String response = gatewayService.serviceA();

        assertEquals("Response From Service A", response);
    }

    @Test
    void serviceB_ShouldReturnExpectedResponse() {
        String response = gatewayService.serviceB();

        assertEquals("Response From Service B", response);
    }
}