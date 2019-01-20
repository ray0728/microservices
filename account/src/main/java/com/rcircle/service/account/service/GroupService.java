package com.rcircle.service.account.service;

import com.rcircle.service.account.mapper.GroupMapper;
import com.rcircle.service.account.model.Group;
import com.rcircle.service.account.model.GroupMemberMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupMapper groupMapper;

    public int create(Group group) {
        groupMapper.create(group);
        return group.getGid();
    }

    public int delete(Group group) {
        int count = 0;
        for (GroupMemberMap gmm : group.getMember()) {
            count += groupMapper.deleteMember(gmm.getId(), gmm.getGid(), gmm.getUid());
        }
        if (count == group.getMember().size()) {
            count = groupMapper.delete(group.getGid());
        }
        if (count == 1) {
            group.reset();
        }
        return count;
    }

    public int changeInfo(Group group) {
        return groupMapper.change(group);
    }

    public int addMember(Group group, int uid) {
        boolean shouldAdd = true;
        for (GroupMemberMap gmm : group.getMember()) {
            if (gmm.getUid() == uid) {
                shouldAdd = false;
                break;
            }
        }
        if (!shouldAdd) {
            return 0;
        }
        GroupMemberMap gmm = new GroupMemberMap();
        gmm.setUid(uid);
        gmm.setGid(group.getGid());
        groupMapper.addMember(gmm);
        if (gmm.getId() == 0) {
            return 0;
        }
        return group.addMember(gmm.getId(), uid);
    }

    public int removeMember(Group group, int uid) {
        int rowid = 0;
        for (GroupMemberMap gmm : group.getMember()) {
            if (gmm.getUid() == uid) {
                rowid = gmm.getId();
                break;
            }
        }
        if (rowid == 0) {
            return 0;
        }
        int row = groupMapper.deleteMember(rowid, group.getGid(), uid);
        if (row <= 0) {
            return 0;
        }
        return group.removeMember(rowid, uid);
    }

    public Group getDetialById(int gid){
        return groupMapper.getDetialById(gid);
    }

    public List<Group> getDetialByKeyWord(String keyword){
        return groupMapper.getDetialByName(keyword);
    }

    public List<Group> getDetialByUid(int uid){
        return groupMapper.getDetialByAccount(uid);
    }
}
