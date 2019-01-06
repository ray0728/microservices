package com.rcircle.service.account.mapper;

import com.rcircle.service.account.model.Group;
import com.rcircle.service.account.model.GroupMemberMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMapper {
    public int create(Group group);

    public int change(Group group);

    public int delete(@Param("gid") int gid);

    public int deleteMember(@Param("id") int id, @Param("gid") int gid, @Param("uid") int uid);

    public int addMember(GroupMemberMap gmm);

    public Group getDetialById(@Param("gid") int gid);

    public List<Group> getDetialByName(@Param("keyword") String keyword);

    public List<Group> getDetialByAccount(@Param("uid") int uid);
}
