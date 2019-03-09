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

    @HystrixCommand(fallbackMethod = "buildFallbackAccountInfo", threadPoolKey = "CreateAccountThreadPool",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1000")})
    public String createAccount(Account account) {
        String info = remoteAccountClient.getInfo(account.getName());
        List<Account> accountList = JSON.parseArray(info, Account.class);
        if (accountList != null && !accountList.isEmpty()) {
            return String.format("failed! username(%s) already exists", account.getName());
        }
        info = remoteAccountClient.create(account.getName(), account.getEmail(), account.getCredentials().toString(), null);
        ErrorData errorData = JSON.parseObject(info, ErrorData.class);
        return errorData.getCode() == ErrorData.INVALID_CODE ? "success" : errorData.toString();
    }

    private String buildFallbackAccountInfo(Account account, Throwable throwable) {
        String failinfo = null;
        if (throwable instanceof com.netflix.hystrix.exception.HystrixTimeoutException) {
            failinfo = "remote service is busy now, please retry it late";
        } else {
            failinfo = throwable.getMessage();
        }
        return "failed! " + failinfo;
    }
}
