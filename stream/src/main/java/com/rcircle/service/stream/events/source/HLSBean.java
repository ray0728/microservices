package com.rcircle.service.stream.events.source;

import com.rcircle.service.stream.events.source.HLSSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HLSBean {
    private HLSSource source;

    @Autowired
    public HLSBean(HLSSource source) {
        this.source = source;
    }

    public void sendHLSSplitFinished(int logid, String filename, boolean ret) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", logid);
        result.put("name", filename);
        result.put("result", ret);
        source.output().send(MessageBuilder.withPayload(result).build());
    }
}
