package org.guvi.service;

import org.springframework.stereotype.Service;

@Service
public class GatewayService {

    public String serviceA() {
        return "Response From Service A";
    }

    public String serviceB() {
        return "Response From Service B";
    }
}
