package com.rcircle.service.message.events.source;

import com.rcircle.service.message.events.models.Sms;
import com.rcircle.service.message.utils.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class SmsBean {
    private SmsSource source;

    @Autowired
    public SmsBean(SmsSource source){
        this.source = source;
    }

    public void send(int uid, String name, int ruid, String msg){
        Sms sms = new Sms();
        sms.setSender_uid(uid);
        sms.setSender_name(name);
        sms.setReceiver_uid(ruid);
        sms.setMessage(msg);
        sms.setDate(SimpleDate.getUTCTime());
        source.output().send(MessageBuilder.withPayload(sms).build());
    }

    public void sendBroadcast(String msg){
        Sms sms = new Sms();
        sms.setSender_uid(Sms.BROADCAST_UID);
        sms.setSender_name(Sms.BROADCAST_NAME);
        sms.setReceiver_uid(Sms.BROADCAST_UID);
        sms.setMessage(msg);
        sms.setDate(SimpleDate.getUTCTime());
        source.output().send(MessageBuilder.withPayload(sms).build());
    }
}
