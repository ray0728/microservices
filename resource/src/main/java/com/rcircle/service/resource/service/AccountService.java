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

    public Account loadUserByUsername(String username) {
        String accountinfo = getAccountInfo(username);
        if (accountinfo.equals("Invalid resources.")) {
            return null;
        }
        List<Account> accountList = JSON.parseArray(accountinfo, Account.class);
        if (accountList == null || accountList.isEmpty()) {
            return null;
        }
        return accountList.get(0);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackAccountInfo",
            threadPoolKey = "AccountInfoThreadPool",
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize", value = "30"),
                            @HystrixProperty(name = "maxQueueSize", value = "10")},
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")})
    public String getAccountInfo(String username) {
        return remoteAccountFeignClient.getInfo(username);
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

    private String buildFallbackAccountInfo() {
        return "Invalid resources.";
    }
}
