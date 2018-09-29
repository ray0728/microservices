package com.ray.service.account.controller;

import com.alibaba.fastjson.JSONObject;
import com.ray.service.account.model.Account;
import com.ray.service.account.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService mAccountService;

    @GetMapping("create")
    public String create(String username, String password, String roles) {
        String ret = null;
        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            return "create params err";
        }
        if (roles == null) {
            roles = "1";
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        String[] rolelist = roles.split("%");
        for (String role : rolelist) {
            account.addRole(Integer.parseInt(role));
        }
        int uid = mAccountService.createAccount(account);
        if (uid == 0) {
            ret = "create failed";
        } else {
            ret = "create success:" + uid;
        }
        return ret;
    }

    @GetMapping("info")
    public String getInfo(String username) {
        List<Account> account = mAccountService.getAccountByUsername(username);
        return JSONObject.toJSONString(account);
    }

    public String updateInfo(){
        return null;
    }

    @GetMapping("me")
    public Authentication showMe(Authentication authentication) {
        return authentication;
    }
}
