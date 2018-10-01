package com.ray.service.account.mapper;

import com.ray.service.account.model.Account;
import com.ray.service.account.model.AccountRoleMap;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper {
    public int create(Account account);

    public int setAccountStatus(@Param("uid") int uid, @Param("status") int status);

    public int deleteAccount(@Param("uid") int uid);

    public List<AccountRoleMap> getAllRoleMapSpecialAccount(@Param("uid")int uid);

    public int deleteAllRoleMap(List<Integer> ids);

    public int deleteRoleMap(@Param("id") int id);

    public int addRoleForAccount(@Param("uid") int uid, @Param("rid") int rid);

    public List<Account> getDetialByName(@Param("username")String username);

    public Account getDetialByUid(@Param("uid")int uid);

    public int updateLoginTime(Account account);

    public List<String> getAllUsername();

    public int changePassword(Account account);
}
