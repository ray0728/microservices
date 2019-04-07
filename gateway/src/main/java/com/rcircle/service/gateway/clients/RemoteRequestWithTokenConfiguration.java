package com.rcircle.service.gateway.clients;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class RemoteRequestWithTokenConfiguration {
    @Bean
    public RequestInterceptor appendTokenInterceptor() {
        return new RemoteRequestWithTokenInterceptor();
    }
}
