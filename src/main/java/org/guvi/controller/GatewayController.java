package org.guvi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    @GetMapping("/service-a")
    public ResponseEntity<String> serviceA() {

        return ResponseEntity.ok(
                "Response From Service A"
        );
    }

    @GetMapping("/service-b")
    public ResponseEntity<String> serviceB() {

        return ResponseEntity.ok(
                "Response From Service B"
        );
    }
}