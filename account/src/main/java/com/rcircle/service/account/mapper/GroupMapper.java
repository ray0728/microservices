package com.rcircle.service.account.mapper;

import com.rcircle.service.account.model.Group;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMapper {
    public int create(String name, String desc, int type);
    public int change(int gid, String name, String desc, int type, int adminuid);
    public int delete(int gid);
    public Group getDetialById(int gid);
    public List<Group> getDetialByName(String keyword);
    public List<Group> getDetialByAccount(int uid);
}
