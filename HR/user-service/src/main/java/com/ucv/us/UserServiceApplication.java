package com.ucv.us;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableMethodSecurity(securedEnabled = true) // for @Secured
//@EnableMethodSecurity(prePostEnabled = true) // for @PreAuthorize if needed later

public class UserServiceApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(UserServiceApplication.class,args);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
