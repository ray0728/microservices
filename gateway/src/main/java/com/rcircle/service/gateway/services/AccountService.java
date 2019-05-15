package com.rcircle.service.gateway.services;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.rcircle.service.gateway.clients.RemoteAccountClient;
import com.rcircle.service.gateway.model.Account;
import com.rcircle.service.gateway.model.ErrorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private RemoteAccountClient remoteAccountClient;

    @HystrixCommand(fallbackMethod = "buildFallbackGetAccountInfo", threadPoolKey = "AccountThreadPool")
    public Account getAccountInfo(int id, String name) {
        String info = remoteAccountClient.getInfo(name, id);
        return JSON.parseObject(info, Account.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackCreateAccount", threadPoolKey = "AccountThreadPool")
    public String createAccount(Account account) {
        Account existAccount = getAccountInfo(0, account.getName());
        if (existAccount != null && !existAccount.hasError()) {
            return String.format("user name(%s) has been used", account.getName());
        }
        existAccount = getAccountInfo(0, account.getEmail());
        if (existAccount != null && !existAccount.hasError()) {
            return String.format("email address(%s) has been used", account.getName());
        }
        String info = remoteAccountClient.create(account.getName(), account.getEmail(), account.getCredentials().toString(), null, account.getProfile());
        ErrorData errorData = JSON.parseObject(info, ErrorData.class);
        return errorData.getCode() == ErrorData.INVALID_CODE ? "success" : errorData.toString();
    }

    @HystrixCommand(fallbackMethod = "buildFallbackAfterLoginSuccess", threadPoolKey = "AccountThreadPool")
    public Account afterLoginSuccess(){
        String ret = remoteAccountClient.refreshTime();
        return JSON.parseObject(ret, Account.class);
    }

    private String autoDetectErrinfo(Throwable throwable){
        String failinfo;
        if (throwable instanceof com.netflix.hystrix.exception.HystrixTimeoutException) {
            failinfo = "remote service is busy now, please retry it late";
        } else {
            failinfo = throwable.getMessage();
        }
        return failinfo;
    }

    private Account buildFallbackAfterLoginSuccess(Throwable throwable){
        Account account = new Account();
        account.setErrinfo(autoDetectErrinfo(throwable));
        return account;
    }

    private Account buildFallbackGetAccountInfo(int id, String name, Throwable throwable){
        Account account = new Account();
        account.setErrinfo(autoDetectErrinfo(throwable));
        return account;
    }

    private String buildFallbackCreateAccount(Account account, Throwable throwable) {
        return autoDetectErrinfo(throwable);
    }
}
