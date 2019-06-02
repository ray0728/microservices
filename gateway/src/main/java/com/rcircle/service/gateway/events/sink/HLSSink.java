package com.rcircle.service.gateway.events.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface HLSSink {
    public String TOPIC = "hls";

    @Input(TOPIC)
    SubscribableChannel input();
}
