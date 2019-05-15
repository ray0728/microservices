package com.rcircle.service.account.controller;

import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.account.model.Account;
import com.rcircle.service.account.model.Role;
import com.rcircle.service.account.service.AccountService;
import com.rcircle.service.account.util.ErrInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Resource
    private AccountService mAccountService;

    private boolean isAdminAccount(String name) {
        Account opAccount = mAccountService.getAccount(name, 0);
        if (opAccount == null) {
            return false;
        }
        List<Role> authorities = opAccount.getRoles();
        for (Role role : authorities) {
            if (role.isAdminRole() || role.isSuperRole()) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("create")
    public String create(Principal principal,
                         @RequestParam(name = "usrname") String username,
                         @RequestParam(name = "email") String email,
                         @RequestParam(name = "passwd") String password,
                         @RequestParam(name = "roles", required = false, defaultValue = "") int[] roles,
                         @RequestParam(name = "profile", required = false, defaultValue = "") String profile
    ) {
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
        account.setEmail(email);
        account.setProfile(profile);
        if (mAccountService.createAccount(account) == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_CREATE_ACCOUNT, "Invalid request parameters.");
        }
        if (isAdminOp) {
            if (roles.length == 0) {
                mAccountService.addRole(account, Role.ID_USER);
            } else {
                mAccountService.addRole(account, roles);
            }
        } else {
            mAccountService.addRole(account, Role.ID_GUEST);
        }
        account.setPassword("******");
        return JSONObject.toJSONString(account);
    }

    @DeleteMapping("delete")
    public String delete(Principal principal,
                         @RequestParam(name = "uid", required = true) int uid) {
        if (uid == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_DELETE_ACCOUNT, "Invalid request parameters.");
        }
        boolean isAdminOp = false;
        if (principal != null) {
            isAdminOp = isAdminAccount(principal.getName());
        }
        if (isAdminOp) {
            Account account = mAccountService.getAccount(null, uid);
            if (account == null) {
                return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_DELETE_ACCOUNT, "Invalid resources.");
            }
            mAccountService.destroyAccount(account);
        }
        return "";
    }

    @PutMapping("change")
    public String changeAccount(Principal principal,
                                @RequestParam(name = "uid", required = true) int uid,
                                @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
                                @RequestParam(name = "roles", required = false) int[] roles) {
        if (uid == 0 || principal == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_CHANGE_ACCOUNT, "Invalid request parameters.");
        }
        Account opAccount = mAccountService.getAccount(principal.getName(), 0);
        if (opAccount == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_CHANGE_ACCOUNT, "Invalid request parameters.");
        }
        int maxRoleLevel = opAccount.getMaxLevelRole();
        if (maxRoleLevel >= Role.ID_ADMIN) {
            Account account = mAccountService.getAccount(null, uid);
            if (account == null) {
                return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_CHANGE_ACCOUNT, "Invalid resources.");
            }
            if (account.getMaxLevelRole() > maxRoleLevel) {
                return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_CHANGE_ACCOUNT, "have no enough permission.");
            }
            if (status != -1) {
                mAccountService.setAccountStatus(account, status);
            }
            if (roles != null && roles.length > 0) {
                mAccountService.addRole(account, roles);
            }
            account.setPassword("******");
            return JSONObject.toJSONString(account);
        } else {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_CHANGE_ACCOUNT, "have no enough permission.");
        }
    }

    @PutMapping("edit")
    public String changePassword(Principal principal,
                                 @RequestParam(name = "email", required = false, defaultValue = "") String email,
                                 @RequestParam(name = "passwd", required = false, defaultValue = "") String password,
                                 @RequestParam(name = "profile", required = false, defaultValue = "") String profile) {
        if (principal == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_EDIT_ACCOUNT, "Invalid request parameters.");
        }
        Account account = mAccountService.getAccount(principal.getName(), 0);
        if (account == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_EDIT_ACCOUNT, "Invalid resources.");
        }
        mAccountService.updateAccountInfo(account.getUid(), email, password, profile);
        account.setPassword("******");
        return JSONObject.toJSONString(account);
    }

    @GetMapping("info")
    public String getInfo(HttpServletRequest request,
                          @RequestParam(name = "username", required = false, defaultValue = "") String username,
                          @RequestParam(name = "uid", required = false, defaultValue = "0") int uid) {
        Account account = mAccountService.getAccount(username, uid);
        String securityFlag = request.getHeader("rc-account-security");
        if (account != null && securityFlag == null) {
            account.setPassword("******");
        }
        return account == null ? null : JSONObject.toJSONString(account);
    }

    @PutMapping("refresh")
    public String refreshTime(Principal principal) {
        if (principal == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_REFRESH_ACCOUNT, "Invalid request parameters.");
        }
        Account account = mAccountService.getAccount(principal.getName(), 0);
        if (account == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_REFRESH_ACCOUNT, "Invalid resources.");
        }
        mAccountService.updateAccountTimeInfo(account);
        account.setPassword("******");
        return JSONObject.toJSONString(account);
    }

    @GetMapping("me")
    public String showMe(Principal principal) {
        Account account = null;
        if (principal != null) {
            account = mAccountService.getAccount(principal.getName(), 0);
            account.setPassword("******");
        }
        return account == null ? "" : JSONObject.toJSONString(account);
    }

}
