package com.rcircle.service.stream.events.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface HLSSource {
    String TOPIC = "hls";

    @Output(TOPIC)
    MessageChannel output();
}
