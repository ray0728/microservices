package com.rcircle.service.account.service;


import com.rcircle.service.account.mapper.AccountMapper;
import com.rcircle.service.account.model.Account;
import com.rcircle.service.account.model.Role;
import com.rcircle.service.account.util.Password;
import com.rcircle.service.account.util.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountMapper mapper;

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
        } else if(isEmailFormat(name)) {
            return mapper.getDetialByName(null, name);
        }else if (name != null && !name.isEmpty()) {
            return mapper.getDetialByName(name, null);
        }
        return null;
    }

    public List<Account> getAllAccounts(){
        return mapper.getAllAccount();
    }

    private boolean isEmailFormat(String email) {
        if(email == null){
            return false;
        }
        return  email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+");
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

    public int updateAccountInfo(int uid, String email, String password, String profile, String resume, String header) {
        Account tmpAccount = new Account();
        tmpAccount.setUid(uid);
        tmpAccount.setEmail(email);
        tmpAccount.setProfile(profile);
        tmpAccount.setResume(resume);
        tmpAccount.setHeader(header);
        tmpAccount.setPassword(Password.crypt(password));
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
