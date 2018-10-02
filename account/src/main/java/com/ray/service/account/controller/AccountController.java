package com.ray.service.account.controller;

import com.alibaba.fastjson.JSONObject;
import com.ray.service.account.model.Account;
import com.ray.service.account.service.AccountService;
import com.ray.service.account.util.ErrInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService mAccountService;

    @PostMapping("create")
    public String create(@RequestParam(name = "usrname", required = false) String username,
                         @RequestParam(name = "passwd", required = false) String password,
                         @RequestParam(name = "roles", required = false, defaultValue = "[1]") int[] roles) {
        String ret = null;
        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_CREATE_ACCOUNT, "Invalid request parameters.");
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        int uid = mAccountService.createAccount(account);
        if(uid == 0){
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_CREATE_ACCOUNT, "Invalid request parameters.");
        }
        mAccountService.autoChangeRoles(account, roles);
        return JSONObject.toJSONString(account);
    }

    @DeleteMapping("delete")
    public String delete(@RequestParam(name = "uid", required = false, defaultValue = "0") int uid) {
        if (uid == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_DELETE_ACCOUNT, "Invalid request parameters.");
        }
        Account account = mAccountService.getAccountByUid(uid);
        if(account == null){
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_DELETE_ACCOUNT, "Invalid resources.");
        }
        mAccountService.destroyAccount(account);
        return JSONObject.toJSONString(account);
    }

    @PutMapping("change")
    public String changeAccount(@RequestParam(name = "uid", required = false, defaultValue = "0") int uid,
                               @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
                               @RequestParam(name = "roles", required = false) int[] roles) {
        if (uid == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_CHANGE_ACCOUNT, "Invalid request parameters.");
        }
        Account account = mAccountService.getAccountByUid(uid);
        if(account == null){
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_CHANGE_ACCOUNT, "Invalid resources.");
        }
        if (status != -1) {
            mAccountService.setAccountStatus(account, status);
        }
        if (roles != null && roles.length > 0) {
            mAccountService.autoChangeRoles(account, roles);
        }
        return JSONObject.toJSONString(account);
    }

    @PutMapping("edit")
    public String changePassword(@RequestParam(name = "uid", required = false, defaultValue = "0") int uid,
                                 @RequestParam(name = "passwd", required = false, defaultValue = "") String password){
        if (uid == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_CHANGE_ACCOUNT, "Invalid request parameters.");
        }
        Account account = mAccountService.getAccountByUid(uid);
        if(account == null){
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_CHANGE_ACCOUNT, "Invalid resources.");
        }
        if (!password.isEmpty()) {
            mAccountService.changeAccountPassword(account, password);
        }
        return JSONObject.toJSONString(account);
    }

    @GetMapping("info")
    public String getInfo(@RequestParam(name = "username", required = true) String username) {
        List<Account> account = mAccountService.getAccountByUsername(username);
        return JSONObject.toJSONString(account);
    }

    @PutMapping("refresh")
    public String refreshTime(@RequestParam(name = "uid", required = false, defaultValue = "0") int uid) {
        if (uid == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_REFRESH_ACCOUNT, "Invalid request parameters.");
        }
        Account account = mAccountService.getAccountByUid(uid);
        if(account == null){
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_REFRESH_ACCOUNT, "Invalid resources.");
        }
        mAccountService.updateAccountTimeInfo(account);
        return JSONObject.toJSONString(account);
    }
}
