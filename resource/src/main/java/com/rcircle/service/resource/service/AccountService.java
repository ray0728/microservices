package com.rcircle.service.resource.service;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.rcircle.service.resource.clients.RemoteAccountFeignClient;
import com.rcircle.service.resource.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountService {
    @Autowired
    private RemoteAccountFeignClient remoteAccountFeignClient;

    public Account loadUser(int uid, String username) {
        String accountinfo = getAccountInfo(uid, username);
        if (accountinfo == null) {
            return null;
        }
        return JSON.parseObject(accountinfo, Account.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackAccountInfo", threadPoolKey = "AccountThreadPool")
    public String getAccountInfo(int uid, String username) {
        return remoteAccountFeignClient.getInfo(username, uid);
    }

    @HystrixCommand(//fallbackMethod = "buildFallbackAccountInfo",
            threadPoolKey = "GroupInfoThreadPool",
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize", value = "30"),
                            @HystrixProperty(name = "maxQueueSize", value = "10")},
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")})
    public String getGroupList() {
        return remoteAccountFeignClient.getGroupsInfo();
//        return  accountRestTemplateClient.getGroupInfo();
    }

    private String buildFallbackAccountInfo(int uid, String username, Throwable throwable) {
        return null;
    }
}
