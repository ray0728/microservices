package com.rcircle.service.gateway.events.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface SmsSink {
    public String TOPIC = "sms";
    @Input(TOPIC)
    SubscribableChannel input();
}
