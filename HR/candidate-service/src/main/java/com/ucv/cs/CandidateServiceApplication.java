package com.ucv.cs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Main application class for the Candidate Service.
 */
@SpringBootApplication
@EnableMethodSecurity(securedEnabled = true)
@EnableFeignClients(basePackages = "com.ucv.cs.client")
public class CandidateServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CandidateServiceApplication.class, args);
    }
}