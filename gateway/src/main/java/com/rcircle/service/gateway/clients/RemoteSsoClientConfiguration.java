package com.rcircle.service.gateway.clients;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoteSsoClientConfiguration {
    @Bean
    public RequestInterceptor OAuth2SsoRequestInterceptor() {
        return new RemoteSsoRequestInterceptor();
    }

}
