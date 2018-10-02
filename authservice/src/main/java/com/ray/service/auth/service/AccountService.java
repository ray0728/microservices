package com.ray.service.auth.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ray.service.auth.clients.RemoteAccountFeignClient;
import com.ray.service.auth.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private RemoteAccountFeignClient remoteAccountFeignClient;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String accountinfo = remoteAccountFeignClient.getInfo(username);
        List<Account> accountList = JSON.parseArray(accountinfo, Account.class);
        if (accountList == null||accountList.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return accountList.get(0).translat();
    }
}
