package com.ucv.ms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.ucv.ms.client")
@EnableMethodSecurity(securedEnabled = true)
public class MatchingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchingServiceApplication.class, args);
    }
}
