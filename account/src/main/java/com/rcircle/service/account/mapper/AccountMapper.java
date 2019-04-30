package com.rcircle.service.account.mapper;

import com.rcircle.service.account.model.Account;
import com.rcircle.service.account.model.AccountRoleMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper {
    public int create(Account account);

    public int setAccountStatus(@Param("uid") long uid, @Param("status") int status);

    public int deleteAccount(@Param("uid") long uid);

    public List<AccountRoleMap> getAllRoleMapSpecialAccount(@Param("uid")long uid);

    public int deleteAllRoleMap(List<Integer> ids);

    public int deleteRoleMap(@Param("id") int id);

    public int addRoleForAccount(@Param("uid") long uid, @Param("rid") int rid);

    public Account getDetialByName(@Param("username")String username);

    public Account getDetialByUid(@Param("uid")long uid);

    public int updateLoginTime(Account account);

    public List<String> getAllUsername();

    public int changePassword(Account account);
}
