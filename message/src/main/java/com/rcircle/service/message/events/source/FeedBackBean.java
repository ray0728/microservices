package com.rcircle.service.message.events.source;

import com.rcircle.service.message.events.models.FeedBack;
import com.rcircle.service.message.utils.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class FeedBackBean {
    private FeedBackSource feedBackSource;

    @Autowired
    public FeedBackBean(FeedBackSource source) {
        feedBackSource = source;
    }

    public void sendFeedBack(String username, String email, String subject, String message) {
        FeedBack feedBack = new FeedBack();
        feedBack.setUsername(username);
        feedBack.setEmail(email);
        feedBack.setSubject(subject);
        feedBack.setMessage(message);
        feedBack.setDate(SimpleDate.getUTCTime());
        feedBackSource.output().send(MessageBuilder.withPayload(feedBack).build());
    }
}
