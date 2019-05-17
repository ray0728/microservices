package com.rcircle.service.gateway.events.listeners;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.gateway.events.sink.NewsSink;
import com.rcircle.service.gateway.events.sink.SmsSink;
import com.rcircle.service.gateway.model.News;
import com.rcircle.service.gateway.model.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(value = {NewsSink.class, SmsSink.class})
public class EventsListener {
    private static final Logger logger = LoggerFactory.getLogger(EventsListener.class);

    @StreamListener(NewsSink.TOPIC)
    public void receiveNews(String string) {
        News news = JSON.parseObject(string, News.class);
        logger.info("Title:{}, author:{}, url:{}, date:{}", news.getTitle(), news.getAuthor(), news.getUrl(), news.getDate());
    }

    @StreamListener(SmsSink.TOPIC)
    public void receiveSms(String str) {
        Sms sms = JSON.parseObject(str, Sms.class);
        logger.info("SEND_UID:{}, RECEIVE_UID:{}, MESSAGE:{}, DATE:{}", sms.getSender_name(), sms.getReceiver_uid(), sms.getMessage(), sms.getDate());
    }
}
