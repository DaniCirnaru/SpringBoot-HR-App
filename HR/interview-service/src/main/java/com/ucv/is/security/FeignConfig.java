package com.ucv.is.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object credentials = auth.getCredentials();
            if (credentials != null) {
                template.header("Authorization", "Bearer " + credentials.toString());
            }
        }
    }

}
