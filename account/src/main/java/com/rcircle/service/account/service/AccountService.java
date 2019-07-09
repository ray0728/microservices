package com.rcircle.service.account.service;


import com.rcircle.service.account.mapper.AccountMapper;
import com.rcircle.service.account.model.Account;
import com.rcircle.service.account.model.Role;
import com.rcircle.service.account.util.NetFile;
import com.rcircle.service.account.util.Password;
import com.rcircle.service.account.util.ResultInfo;
import com.rcircle.service.account.util.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class AccountService {
    @Value("${account.dir.root}")
    private String saveDirRoot;

    @Autowired
    AccountMapper mapper;

    private String autoDetectErrorMessageAfterUpload(int error) {
        String message = null;
        switch (error) {
            case ResultInfo.CODE_OPEN_FILE:
                message = "abort";
                break;
            case ResultInfo.CODE_SAVE_FILE:
                message = "abort";
                break;
            case ResultInfo.CODE_CHECK_SUM:
                message = "resend";
                break;
        }
        return message;
    }

    public String updateAvatar(int uid, String checksum, MultipartFile file) {
        String root = NetFile.getDirAbsolutePath(saveDirRoot, String.valueOf(uid));
        String ret = root;
        try {
            int error = NetFile.saveFileFromNet(root, "avatar", checksum, file);
            String message = autoDetectErrorMessageAfterUpload(error);
            if(message != null) {
                ret = ResultInfo.assembleJson(ResultInfo.ErrType.EXCEPTION, error, message);
            }
        } catch (IOException e) {
            ret = ResultInfo.assembleJson(ResultInfo.ErrType.EXCEPTION, 0, e.getMessage());
        }
        return ret;
    }

    public long createAccount(Account account) {
        long uid = 0;
        if (mapper.getDetialByName(account.getUsername(), account.getEmail()) == null) {
            account.setPassword(Password.crypt(account.getPassword()));
            account.setFirsttime(SimpleDate.getUTCTime());
            mapper.create(account);
            uid = account.getUid();
        }
        return uid;
    }

    public Account getAccount(String name, int uid) {
        if (uid != 0) {
            return mapper.getDetialByUid(uid);
        } else if (isEmailFormat(name)) {
            return mapper.getDetialByName(null, name);
        } else if (name != null && !name.isEmpty()) {
            return mapper.getDetialByName(name, null);
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        return mapper.getAllAccount();
    }

    private boolean isEmailFormat(String email) {
        if (email == null) {
            return false;
        }
        return email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+");
    }

    public int updateAccountTimeInfo(Account account) {
        account.appendOneTimes();
        account.setLastlogin(SimpleDate.getUTCTime());
        return mapper.updateLoginTime(account);
    }

    public int destroyAccount(Account account) {
        for (Role role : account.getRoles()) {
            mapper.deleteRoleMap(role.getMid());
        }
        int ret = mapper.deleteAccount(account.getUid());
        account.reset();
        return ret;
    }

    public int setAccountStatus(Account account, int status) {
        account.setStatus(status);
        return mapper.setAccountStatus(account.getUid(), status);
    }

    public int updateAccountInfo(int uid, String email, String password, String signature, String resume, String avatar) {
        Account tmpAccount = new Account();
        tmpAccount.setUid(uid);
        if(email != null) {
            tmpAccount.setEmail(email);
        }
        if(signature != null) {
            tmpAccount.setSignature(signature);
        }
        if(resume != null) {
            tmpAccount.setResume(resume);
        }
        if(avatar != null) {
            tmpAccount.setAvatar(avatar);
        }
        if(password != null) {
            tmpAccount.setPassword(Password.crypt(password));
        }
        return mapper.updateAccount(tmpAccount);
    }

    public int addRole(Account account, int... rids) {
        boolean isSkip = false;
        int count = 0;
        for (int rid : rids) {
            isSkip = false;
            for (int i = 0; i < account.getRoles().size(); i++) {
                if (account.getRoles().get(i).getRid() == rid) {
                    isSkip = true;
                    break;
                }
            }
            if (!isSkip) {
                count++;
                mapper.addRoleForAccount(account.getUid(), rid);
                account.addRole(rid);
            }
        }
        return count;
    }

    public int deleteRole(Account account, int... rids) {
        int count = 0;
        for (int rid : rids) {
            for (int i = 0; i < account.getRoles().size(); i++) {
                if (account.getRoles().get(i).getRid() == rid) {
                    mapper.deleteRoleMap(account.getRoles().get(i).getMid());
                    account.deleteRole(rid);
                    break;
                }
            }
        }
        return count;
    }

    public List<String> getAllUsername() {
        return mapper.getAllUsername();
    }
}
