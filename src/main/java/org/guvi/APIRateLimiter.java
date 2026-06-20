package org.guvi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class APIRateLimiter {
    public static void main(String[] args) {
        SpringApplication.run(APIRateLimiter.class, args);
    }
}