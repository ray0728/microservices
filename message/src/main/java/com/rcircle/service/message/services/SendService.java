package com.rcircle.service.message.services;

import com.rcircle.service.message.events.source.FeedBackBean;
import com.rcircle.service.message.events.source.HlsBean;
import com.rcircle.service.message.events.source.NewsBean;
import com.rcircle.service.message.events.source.SmsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendService {
    @Autowired
    private SmsBean smsBean;

    @Autowired
    private NewsBean newsBean;

    @Autowired
    private HlsBean hlsBean;

    @Autowired
    private FeedBackBean feedBackBean;

    public void sendHLSSplitFinished(int logid, String filename, boolean ret) {
        hlsBean.sendHLSSplitFinished(logid, filename, ret);
    }

    public void sendBreakNews(int level, String title, String author, String url) {
        newsBean.sendBreakNews(level, title, author, url);
    }

    public void sendNews(String title, String author, String url) {
        newsBean.sendNews(title, author, url);
    }

    public void sendSms(int uid, String name, int ruid, String msg) {
        smsBean.send(uid, name, ruid, msg);
    }

    public void sendBroadcast(String msg) {
        smsBean.sendBroadcast(msg);
    }

    public void sendFeedBack(String username, String email, String subject, String message) {
        feedBackBean.sendFeedBack(username, email, subject, message);
    }
}
