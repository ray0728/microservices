package com.ray.service.account.service;


import com.ray.service.account.mapper.AccountMapper;
import com.ray.service.account.model.Account;
import com.ray.service.account.model.Authority;
import com.ray.service.account.util.Password;
import com.ray.service.account.util.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountMapper mapper;

    public int createAccount(Account account) {
        int uid = 0;
        if (mapper.getDetialByName(account.getUsername()) == null) {
            account.setPassword(Password.crypt(account.getPassword()));
            account.setFirsttime(SimpleDate.getCurrentTime());
            mapper.create(account);
            uid = account.getUid();
            Iterator<GrantedAuthority> iter = account.translat().getAuthorities().iterator();
            while (iter.hasNext()) {
                Authority authority = (Authority) iter.next();
                mapper.addRoleForAccount(uid, authority.getId());
            }
        }
        return uid;
    }

    public Account getAccountByUsername(String username) {
        return mapper.getDetialByName(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = getAccountByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return account.translat();
    }
}
