package com.rcircle.service.message.services;

import com.rcircle.service.message.events.source.SmsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    @Autowired
    private SmsBean smsBean;

    public void sendSms(int uid, String name, int ruid, String msg){
        smsBean.send(uid, name, ruid, msg);
    }

    public void sendBroadcast(String msg){
        smsBean.sendBroadcast(msg);
    }
}
