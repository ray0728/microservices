package com.rcircle.service.account.controller;

import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.account.model.Account;
import com.rcircle.service.account.model.Authority;
import com.rcircle.service.account.service.AccountService;
import com.rcircle.service.account.util.ErrInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService mAccountService;

    private boolean isAdminAccount(String name) {
        List<Account> opAccountList = mAccountService.getAccountByUsername(name);
        for (Account opaccount : opAccountList) {
            List<Authority> authorities = opaccount.getRoles();
            for (Authority role : authorities) {
                if (role.isAdminRole() || role.isSuperRole()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Account getOpAccount(String name) {
        Account account = null;
        List<Account> opAccountList = mAccountService.getAccountByUsername(name);
        if (opAccountList != null && opAccountList.size() > 0) {
            account = opAccountList.get(0);
        }
        return account;
    }


    @PostMapping("create")
    public String create(Principal principal, @RequestParam(name = "usrname", required = true) String username,
                         @RequestParam(name = "passwd", required = true) String password,
                         @RequestParam(name = "roles", required = false, defaultValue = "") int[] roles) {
        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_CREATE_ACCOUNT, "Invalid request parameters.");
        }
        boolean isAdminOp = false;
        if (principal != null) {
            isAdminOp = isAdminAccount(principal.getName());
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        if (mAccountService.createAccount(account) == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_CREATE_ACCOUNT, "Invalid request parameters.");
        }
        if (isAdminOp) {
            if(roles.length == 0){
                mAccountService.autoChangeRoles(account, Authority.ID_USER);
            }else {
                mAccountService.autoChangeRoles(account, roles);
            }
        } else {
            mAccountService.autoChangeRoles(account, Authority.ID_GUEST);
        }
        account.setPassword("******");
        return JSONObject.toJSONString(account);
    }

    @DeleteMapping("delete")
    public String delete(Principal principal,
                         @RequestParam(name = "uid", required = false, defaultValue = "0") int uid) {
        if (uid == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_DELETE_ACCOUNT, "Invalid request parameters.");
        }
        boolean isAdminOp = false;
        if (principal != null) {
            isAdminOp = isAdminAccount(principal.getName());
        }
        if (isAdminOp) {
            Account account = mAccountService.getAccountByUid(uid);
            if (account == null) {
                return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_DELETE_ACCOUNT, "Invalid resources.");
            }
            mAccountService.destroyAccount(account);
        }
        return "";
    }

    @PutMapping("change")
    public String changeAccount(Principal principal,
                                @RequestParam(name = "uid", required = true) long uid,
                                @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
                                @RequestParam(name = "roles", required = false) int[] roles) {
        if (uid == 0 || principal == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_CHANGE_ACCOUNT, "Invalid request parameters.");
        }
        Account opAccount = getOpAccount(principal.getName());
        if(opAccount == null){
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_CHANGE_ACCOUNT, "Invalid request parameters.");
        }
        int maxRoleLevel = opAccount.getMaxLevelRole();
        if (maxRoleLevel != Authority.ID_GUEST) {
            Account account = mAccountService.getAccountByUid(uid);
            if (account == null) {
                return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_CHANGE_ACCOUNT, "Invalid resources.");
            }
            for (Authority role : account.getRoles()) {
                if (role.getId() > maxRoleLevel) {
                    return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_CHANGE_ACCOUNT, "have not enought permission.");
                }
            }
            if (status != -1) {
                mAccountService.setAccountStatus(account, status);
            }
            if (roles != null && roles.length > 0) {
                mAccountService.autoChangeRoles(account, roles);
            }
            account.setPassword("******");
            return JSONObject.toJSONString(account);
        } else {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_CHANGE_ACCOUNT, "have not enought permission.");
        }
    }

    @PutMapping("edit")
    public String changePassword(Principal principal,
                                 @RequestParam(name = "passwd", required = false, defaultValue = "") String password) {
        if (principal == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_EDIT_ACCOUNT, "Invalid request parameters.");
        }
        List<Account> accounts = mAccountService.getAccountByUsername(principal.getName());
        if (accounts == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_EDIT_ACCOUNT, "Invalid resources.");
        }
        if (!password.isEmpty()) {
            mAccountService.changeAccountPassword(accounts.get(0), password);
        }
        accounts.get(0).setPassword("******");
        return JSONObject.toJSONString(accounts.get(0));
    }

    @GetMapping("info")
    public String getInfo(@RequestParam(name = "username", required = true) String username) {
        List<Account> accounts = mAccountService.getAccountByUsername(username);
//        if(accounts != null){
//            for(Account account:accounts){
//                account.setPassword("******");
//            }
//        }
        return JSONObject.toJSONString(accounts);
    }

    @PutMapping("refresh")
    public String refreshTime(Principal principal) {
        if (principal == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_REFRESH_ACCOUNT, "Invalid request parameters.");
        }
        List<Account> accounts = mAccountService.getAccountByUsername(principal.getName());
        if (accounts == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_REFRESH_ACCOUNT, "Invalid resources.");
        }
        mAccountService.updateAccountTimeInfo(accounts.get(0));
        accounts.get(0).setPassword("******");
        return JSONObject.toJSONString(accounts.get(0));
    }

    @GetMapping("me")
    public String showMe(Principal principal) {
        Account account = null;
        if(principal != null){
            account = getOpAccount(principal.getName());
            account.setPassword("******");
        };
        return account == null? "":JSONObject.toJSONString(account);
    }

}
