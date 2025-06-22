package com.ucv.is;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.ucv.is.client")
@EnableMethodSecurity(securedEnabled = true)
public class InterviewServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InterviewServiceApplication.class, args);
    }
}
