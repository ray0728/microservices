package com.rcircle.service.message.services;

import com.rcircle.service.message.events.source.HlsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HlsService {
    @Autowired
    private HlsBean hlsBean;

    public void sendHLSSplitFinished(int logid, String filename, boolean ret){
        hlsBean.sendHLSSplitFinished(logid, filename, ret);
    }
}
