package com.ray.service.resource.service;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ray.service.resource.clients.RemoteAccountFeignClient;
import com.ray.service.resource.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountService {
    @Autowired
    private RemoteAccountFeignClient remoteAccountFeignClient;

    public Account loadUserByUsername(String username){
        String accountinfo = getAccountInfo(username);
        if(accountinfo.equals("Invalid resources.")){
            return null;
        }
        List<Account> accountList = JSON.parseArray(accountinfo, Account.class);
        if (accountList == null||accountList.isEmpty()) {
            return null;
        }
        return accountList.get(0);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackAccountInfo", threadPoolKey = "AccountInfoThreadPool")
    public String getAccountInfo(String username){
        return remoteAccountFeignClient.getInfo(username);
    }

    private String buildFallbackAccountInfo(){
        return "Invalid resources.";
    }
}
