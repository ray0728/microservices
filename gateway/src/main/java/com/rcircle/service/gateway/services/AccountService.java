package com.rcircle.service.gateway.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.gateway.clients.RemoteAccountClient;
import com.rcircle.service.gateway.model.Account;
import com.rcircle.service.gateway.model.ResultData;
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

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllAcounts", threadPoolKey = "AccountThreadPool")
    public List<Account> getAllAccounts(){
        String ret = remoteAccountClient.getInfo("", 0);
        return JSONArray.parseArray(ret, Account.class);
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
        ResultData errorData = JSON.parseObject(info, ResultData.class);
        return errorData.isSuccess()? "success" : errorData.toString();
    }

    @HystrixCommand(fallbackMethod = "buildFallbackAfterLoginSuccess", threadPoolKey = "AccountThreadPool")
    public Account afterLoginSuccess(){
        String ret = remoteAccountClient.refreshTime();
        ResultData data = JSON.parseObject(ret, ResultData.class);
        return JSON.parseObject(data.getMap().get("account").toString(), Account.class);
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

    private List<Account> buildFallbackGetAllAcounts(Throwable throwable){
        return null;
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
