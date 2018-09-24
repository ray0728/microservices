package com.ray.service.account.mapper;

import com.ray.service.account.model.Account;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper {
    public int create(Account account);

    public int addRoleForAccount(@Param("uid") int uid, @Param("rid") int rid);

    public Account getDetialByName(String username);

    public List<String> getAllUsername();
}
