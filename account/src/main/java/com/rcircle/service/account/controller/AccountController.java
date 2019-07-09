package com.rcircle.service.account.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.account.model.Account;
import com.rcircle.service.account.model.ResultData;
import com.rcircle.service.account.model.Role;
import com.rcircle.service.account.service.AccountService;
import com.rcircle.service.account.util.ResultInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    @PostMapping("avatar/{uid}")
    public String uploadAvatar(Principal principal,
                               MultipartFile file,
                               @PathVariable("uid") int uid,
                               @RequestParam(name = "checksum") String checksum) {
        Account opAccount = mAccountService.getAccount(principal.getName(), 0);
        if (opAccount == null || opAccount.getUid() != uid) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_UPLOAD_AVATAR, "Invalid resources.");
        }
        return mAccountService.updateAvatar(uid, checksum, file);
    }


    @PostMapping(value = "create")
    public String create(Principal principal,
                         @RequestParam(name = "file", required = false) MultipartFile file,
                         @RequestParam(name = "username") String username,
                         @RequestParam(name = "email") String email,
                         @RequestParam(name = "passwd") String password,
                         @RequestParam(name = "roles", required = false, defaultValue = "") int[] roles,
                         @RequestParam(name = "signature", required = false, defaultValue = "") String profile,
                         @RequestParam(name = "resume", required = false, defaultValue = "") String resume,
                         @RequestParam(name = "checksum", required = false, defaultValue = "") String checksum
    ) {
        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_CREATE_ACCOUNT, "Invalid request parameters.");
        }
        boolean isAdminOp = false;
        if (principal != null) {
            isAdminOp = isAdminAccount(principal.getName());
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setEmail(email);
        account.setSignature(profile);
        account.setResume(resume);
        if (mAccountService.createAccount(account) == 0) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_CREATE_ACCOUNT, "Invalid request parameters.");
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
        if (!checksum.isEmpty() && file != null) {
            String avatarurl = mAccountService.updateAvatar(account.getUid(), checksum, file);
            account.setAvatar(avatarurl);
            mAccountService.updateAccountInfo(account.getUid(), null, null, null, null, avatarurl);
        }
        ResultData data = new ResultData();
        data.setCode(ResultInfo.CODE_CREATE_ACCOUNT);
        data.setType(ResultInfo.translate(ResultInfo.ErrType.SUCCESS));
        data.addToMap("account", account);
        return JSONObject.toJSONString(data);
    }

    @DeleteMapping("delete")
    public String delete(Principal principal,
                         @RequestParam(name = "uid", required = true) int uid) {
        if (uid == 0) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_DELETE_ACCOUNT, "Invalid request parameters.");
        }
        boolean isAdminOp = false;
        if (principal != null) {
            isAdminOp = isAdminAccount(principal.getName());
        }
        if (isAdminOp) {
            Account account = mAccountService.getAccount(null, uid);
            if (account == null) {
                return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_DELETE_ACCOUNT, "Invalid resources.");
            }
            mAccountService.destroyAccount(account);
        }
        return ResultInfo.assembleSuccessJson(ResultInfo.CODE_DELETE_ACCOUNT, null, null, null);
    }

    @PutMapping("change")
    public String changeAccount(Principal principal,
                                @RequestParam(name = "uid", required = true) int uid,
                                @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
                                @RequestParam(name = "roles", required = false) int[] roles) {
        if (uid == 0 || principal == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_CHANGE_ACCOUNT, "Invalid request parameters.");
        }
        Account opAccount = mAccountService.getAccount(principal.getName(), 0);
        if (opAccount == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_CHANGE_ACCOUNT, "Invalid request parameters.");
        }
        int maxRoleLevel = opAccount.getMaxLevelRole();
        if (maxRoleLevel >= Role.ID_ADMIN) {
            Account account = mAccountService.getAccount(null, uid);
            if (account == null) {
                return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_CHANGE_ACCOUNT, "Invalid resources.");
            }
            if (account.getMaxLevelRole() > maxRoleLevel) {
                return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_CHANGE_ACCOUNT, "have no enough permission.");
            }
            if (status != -1) {
                mAccountService.setAccountStatus(account, status);
            }
            if (roles != null && roles.length > 0) {
                mAccountService.addRole(account, roles);
            }
            account.setPassword("******");
            return ResultInfo.assembleSuccessJson(ResultInfo.CODE_CHANGE_ACCOUNT, "finished", "account", account);
        } else {
            return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_CHANGE_ACCOUNT, "have no enough permission.");
        }
    }

    @PutMapping("edit")
    public String changePassword(Principal principal,
                                 @RequestParam(name = "email", required = false, defaultValue = "") String email,
                                 @RequestParam(name = "passwd", required = false, defaultValue = "") String password,
                                 @RequestParam(name = "signature", required = false, defaultValue = "") String profile,
                                 @RequestParam(name = "resume", required = false, defaultValue = "") String resume,
                                 @RequestParam(name = "avatar", required = false, defaultValue = "") String header) {
        if (principal == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_EDIT_ACCOUNT, "Invalid request parameters.");
        }
        Account account = mAccountService.getAccount(principal.getName(), 0);
        if (account == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_EDIT_ACCOUNT, "Invalid resources.");
        }
        mAccountService.updateAccountInfo(account.getUid(), email, password, profile, resume, header);
        account.setPassword("******");
        return ResultInfo.assembleSuccessJson(ResultInfo.CODE_EDIT_ACCOUNT, "finished", "account", account);
    }

    @GetMapping("info")
    public String getInfo(HttpServletRequest request,
                          @RequestParam(name = "username", required = false, defaultValue = "") String username,
                          @RequestParam(name = "uid", required = false, defaultValue = "0") int uid) {
        String ret = null;
        if (username.isEmpty() && uid == 0) {
            List<Account> accounts = mAccountService.getAllAccounts();
            for(Account account: accounts){
                account.hideSensitiveInfo();
            }
            ret = accounts == null ? null : JSONArray.toJSONString(accounts);
        } else {
            Account account = mAccountService.getAccount(username, uid);
            String securityFlag = request.getHeader("rc-account-security");
            if (account != null && securityFlag == null) {
                account.hideSensitiveInfo();
            }
            ret = account == null ? null : JSONObject.toJSONString(account);
        }
        return ret;
    }

    @GetMapping("avatar/{uid}")
    public ResponseEntity getAvatar(Principal principal, @PathVariable("uid") int uid){
        Account account = mAccountService.getAccount(null, uid);
        String errinfo = null;
        try {
            return  account.getAvatar().isEmpty() ? null: createResponseEntity("image/jpg",  account.getAvatar() + File.separatorChar + "avatar");
        }catch (IOException e){
            errinfo = e.getMessage();
        }
        return ResponseEntity.status(404).body(errinfo);
    }

    private ResponseEntity createResponseEntity(String type, String filePath) throws IOException {
        MediaType mediaType = MediaType.parseMediaType(type);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        inputStream.close();
        return new ResponseEntity(bytes, headers, HttpStatus.OK);
    }


    @PutMapping("refresh")
    public String refreshTime(Principal principal) {
        if (principal == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_REFRESH_ACCOUNT, "Invalid request parameters.");
        }
        Account account = mAccountService.getAccount(principal.getName(), 0);
        if (account == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_REFRESH_ACCOUNT, "Invalid resources.");
        }
        mAccountService.updateAccountTimeInfo(account);
        account.setPassword("******");
        return ResultInfo.assembleSuccessJson(ResultInfo.CODE_REFRESH_ACCOUNT, "finished", "account", account);
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
