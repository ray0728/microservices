package com.rcircle.service.gateway.services;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.gateway.clients.RemoteResourceClient;
import com.rcircle.service.gateway.model.Quotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferenceService {
    public static final int TYPE_QUOTATION_MINI_ID = 0;
    public static final int TYPE_QUOTATION_MAX_ID = 1;
    public static final int TYPE_QUOTATION_RAND_ID = 2;
    @Autowired
    private RemoteResourceClient remoteResourceClient;

    @HystrixCommand(fallbackMethod = "buildGetRandomQuotation", threadPoolKey = "ReferenceThreadPool")
    public Quotation getRandomQuotation(){
        String ret = remoteResourceClient.getQuotation(TYPE_QUOTATION_RAND_ID, 0);
        return JSON.parseObject(ret, Quotation.class);
    }

    public Quotation buildGetRandomQuotation(){
        Quotation quotation = new Quotation();
        quotation.setDesc_english("Together we've tried,As we stood side by side I knew we'd build a new world,A world of hope forever after.");
        quotation.setAuthor("Stefanie Sun");
        quotation.setSource("WE WILL GET THERE");
        return quotation;
    }
}
