package com.rcircle.service.message;

import com.rcircle.service.message.events.source.FeedBackSource;
import com.rcircle.service.message.events.source.HlsSource;
import com.rcircle.service.message.events.source.NewsSource;
import com.rcircle.service.message.events.source.SmsSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
@EnableBinding(value = {NewsSource.class, SmsSource.class, HlsSource.class, FeedBackSource.class})
public class MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }

}
