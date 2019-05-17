package com.rcircle.service.message.events.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface SmsSource {
    String TOPIC = "sms";
    @Output(TOPIC)
    MessageChannel output();
}
