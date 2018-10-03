package com.ray.service.account.service;


import com.ray.service.account.mapper.AccountMapper;
import com.ray.service.account.model.Account;
import com.ray.service.account.model.AccountRoleMap;
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
import java.util.LinkedList;
import java.util.List;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountMapper mapper;

    public int createAccount(Account account) {
        int uid = 0;
        if (mapper.getDetialByName(account.getUsername()).isEmpty()) {
            account.setPassword(Password.crypt(account.getPassword()));
            account.setFirsttime(SimpleDate.getCurrentTime());
            mapper.create(account);
            uid = account.getUid();
        }
        return uid;
    }

    public List<Account> getAccountByUsername(String username) {
        return mapper.getDetialByName(username);
    }

    public Account getAccountByUid(int uid) {
        return mapper.getDetialByUid(uid);
    }

    public int updateAccountTimeInfo(Account account) {
        account.appendOneTimes();
        account.setLastlogin(SimpleDate.getCurrentTime());
        return mapper.updateLoginTime(account);
    }

    public int destroyAccount(Account account) {
        List<AccountRoleMap> list = mapper.getAllRoleMapSpecialAccount(account.getUid());
        if (list != null && !list.isEmpty()) {
            List<Integer> armids = new LinkedList<>();
            for (AccountRoleMap armap : list) {
                armids.add(armap.getId());
            }
            mapper.deleteAllRoleMap(armids);
        }
        int ret = mapper.deleteAccount(account.getUid());
        account.reset();
        return ret;
    }

    public int setAccountStatus(Account account, int status) {
        account.setStatus(status);
        return mapper.setAccountStatus(account.getUid(), status);
    }

    public int changeAccountPassword(Account account, String password) {
        account.setPassword(Password.crypt(password));
        return mapper.changePassword(account);
    }

    public int autoChangeRoles(Account account, int... rids) {
        Authority[] roles = account.getRoles().toArray(new Authority[]{});
        boolean isSkip = false;
        for (Authority ga : roles) {
            isSkip = false;
            for (int rid : rids) {
                if (ga.getId() == rid) {
                    isSkip = true;
                    break;
                }
            }
            if (!isSkip) {
                rmoveRoleFromAccount(account.getUid(), ga.getId());
                account.deleteRole(ga.getId());
            }
        }
        isSkip = false;
        for (int rid : rids) {
            isSkip = false;
            for (Authority ga : roles) {
                if (ga.getId() == rid) {
                    isSkip = true;
                    break;
                }
            }
            if (!isSkip) {
                mapper.addRoleForAccount(account.getUid(), rid);
                account.addRole(rid);
            }
        }
        return account.getRoles().size();
    }

    private int rmoveRoleFromAccount(int uid, int rid) {
        int count = 0;
        List<AccountRoleMap> list = mapper.getAllRoleMapSpecialAccount(uid);
        if (list != null && !list.isEmpty()) {
            for (AccountRoleMap armap : list) {
                if (armap.getRid() == rid) {
                    count += mapper.deleteRoleMap(armap.getId());
                }
            }
        }
        return count;
    }

    public List<String> getAllUsername() {
        return mapper.getAllUsername();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Account> accounts = getAccountByUsername(username);
        if (accounts == null || accounts.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return accounts.get(0).translat();
    }
}
