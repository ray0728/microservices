package com.rcircle.service.gateway.events.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface NewsSink {
    public String TOPIC = "news";

    @Input(TOPIC)
    SubscribableChannel input();
}
