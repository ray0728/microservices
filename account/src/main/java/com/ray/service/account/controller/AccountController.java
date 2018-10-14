package com.ray.service.account.controller;

import com.alibaba.fastjson.JSONObject;
import com.ray.service.account.model.Account;
import com.ray.service.account.model.Authority;
import com.ray.service.account.service.AccountService;
import com.ray.service.account.util.ErrInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService mAccountService;

    @PostMapping("create")
    public String create(Authentication authentication,
                         @RequestParam(name = "usrname", required = false) String username,
                         @RequestParam(name = "passwd", required = false) String password,
                         @RequestParam(name = "roles", required = false, defaultValue = "[2]") int[] roles) {
        String ret = null;
        boolean isAdminOp = false;
        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_CREATE_ACCOUNT, "Invalid request parameters.");
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        if (mAccountService.createAccount(account) == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_CREATE_ACCOUNT, "Invalid request parameters.");
        }
        if (authentication != null) {
            List<Authority> authorities = (List<Authority>) authentication.getAuthorities();
            for (Authority role : authorities) {
                if (role.isAdminRole() || role.isSuperRole()) {
                    isAdminOp = true;
                    break;
                }
            }
        }
        if (isAdminOp) {
            mAccountService.autoChangeRoles(account, roles);
        } else {
            mAccountService.autoChangeRoles(account, Authority.ID_USER);
        }
        return JSONObject.toJSONString(account);
    }

    @DeleteMapping("delete")
    public String delete(Authentication authentication,
                         @RequestParam(name = "uid", required = false, defaultValue = "0") int uid) {
        boolean isAdminOp = false;
        if (uid == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_DELETE_ACCOUNT, "Invalid request parameters.");
        }
        List<Authority> authorities = (List<Authority>) authentication.getAuthorities();
        for (Authority role : authorities) {
            if (role.isAdminRole() || role.isSuperRole()) {
                isAdminOp = true;
                break;
            }
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
    public String changeAccount(Authentication authentication,
                                @RequestParam(name = "uid", required = true) long uid,
                                @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
                                @RequestParam(name = "roles", required = false) int[] roles) {
        int maxRoleLevel = Authority.ID_GUEST;
        if (uid == 0 || authentication == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_CHANGE_ACCOUNT, "Invalid request parameters.");
        }
        List<Authority> authorities = (List<Authority>) authentication.getAuthorities();
        for (Authority role : authorities) {
            if (role.isAdminRole()) {
                maxRoleLevel = Authority.ID_ADMIN;
            } else if (role.isSuperRole()) {
                maxRoleLevel = Authority.ID_SUPER;
            }
        }
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
        List<Account> account = mAccountService.getAccountByUsername(principal.getName());
        if (account == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_EDIT_ACCOUNT, "Invalid resources.");
        }
        if (!password.isEmpty()) {
            mAccountService.changeAccountPassword(account.get(0), password);
        }
        return JSONObject.toJSONString(account);
    }

    @GetMapping("info")
    public String getInfo(@RequestParam(name = "username", required = true) String username) {
        List<Account> account = mAccountService.getAccountByUsername(username);
        return JSONObject.toJSONString(account);
    }

    @PutMapping("refresh")
    public String refreshTime(Principal principal) {
        if (principal == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_REFRESH_ACCOUNT, "Invalid request parameters.");
        }
        List<Account> account = mAccountService.getAccountByUsername(principal.getName());
        if (account == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_REFRESH_ACCOUNT, "Invalid resources.");
        }
        mAccountService.updateAccountTimeInfo(account.get(0));
        return JSONObject.toJSONString(account);
    }

    @GetMapping("me")
    public Authentication showMe(Authentication authentication) {
        return authentication;
    }

}
