package com.rcircle.service.message.events.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface NewsSource {
    String TOPIC = "news";

    @Output(TOPIC)
    MessageChannel output();
}
