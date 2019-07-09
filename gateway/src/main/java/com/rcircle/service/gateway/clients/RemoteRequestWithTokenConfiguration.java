package com.rcircle.service.gateway.clients;

import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;

public class RemoteRequestWithTokenConfiguration {
    @Bean
    public RequestInterceptor appendTokenInterceptor() {
        return new RemoteRequestWithTokenInterceptor();
    }

//    @Autowired
//    private ObjectFactory<HttpMessageConverters> messageConverters;
//
//    @Bean
//    public Encoder feignFormEncoder () {
//        return new SpringFormEncoder(new SpringEncoder(messageConverters));
////        return new SpringFormEncoder();
//    }
}
