package com.rcircle.service.auth.service;


import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.auth.clients.RemoteAccountFeignClient;
import com.rcircle.service.auth.model.Account;
import com.rcircle.service.auth.utils.HttpContext;
import com.rcircle.service.auth.utils.HttpContextHolder;
import com.rcircle.service.auth.utils.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private RemoteAccountFeignClient remoteAccountFeignClient;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String accountinfo = getAccountInfo(username);
        Account account = null;
        if(accountinfo != null) {
            account = JSON.parseObject(accountinfo, Account.class);
        }
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return account.translat();
    }

    @HystrixCommand(fallbackMethod = "buildFallbackAccountInfo", threadPoolKey = "AccountInfoThreadPool")
    public String getAccountInfo(String username){
       HttpContextHolder.getContext().setValue(HttpContext.QUERY_ACCOUNT_SECU, SimpleDate.getUTCString());
       String info = remoteAccountFeignClient.getInfo(username);
       HttpContextHolder.getContext().clear();
       return info;
    }

    private String buildFallbackAccountInfo(Throwable throwable){
        return null;
    }
}
