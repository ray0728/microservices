package com.rcircle.service.auth.service;


import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.rcircle.service.auth.clients.RemoteAccountFeignClient;
import com.rcircle.service.auth.model.Account;
import com.rcircle.service.auth.util.ErrInfo;
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
        if(accountinfo.equals("Invalid resources.")){
            throw new UsernameNotFoundException("Invalid resources.");
        }
        List<Account> accountList = JSON.parseArray(accountinfo, Account.class);
        if (accountList == null||accountList.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return accountList.get(0).translat();
    }

    @HystrixCommand(fallbackMethod = "buildFallbackAccountInfo", threadPoolKey = "AccountInfoThreadPool")
    public String getAccountInfo(String username){
        return remoteAccountFeignClient.getInfo(username);
    }

    private String buildFallbackAccountInfo(){
        return "Invalid resources.";
    }
}
