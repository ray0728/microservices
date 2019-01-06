package com.rcircle.service.account.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private int gid = 0;
    private String name = "";
    private String desc = "";
    private int type = 0;
    private int level = 0;
    private int admin_uid = 0;
    private long create_date = 0;
    private List<GroupMemberMap> member = null;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAdmin_uid() {
        return admin_uid;
    }

    public void setAdmin_uid(int admin_uid) {
        this.admin_uid = admin_uid;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public List<GroupMemberMap> getMember() {
        if (member == null) {
            member = new ArrayList<>();
        }
        return member;
    }

    public void setMember(List<GroupMemberMap> member) {
        this.member = member;
    }

    public int addMember(int rowid, int uid) {
        GroupMemberMap gmm = new GroupMemberMap();
        gmm.setGid(gid);
        gmm.setUid(uid);
        gmm.setId(rowid);
        getMember().add(gmm);
        return getMember().size();
    }

    public int removeMember(int rowid, int uid){
        for(GroupMemberMap gmm:getMember()){
            if(gmm.getId() == rowid && gmm.getUid() == uid){
                getMember().remove(gmm);
                break;
            }
        }
        return getMember().size();
    }

    public void reset() {
        name = null;
        desc = null;
        type = 0;
        admin_uid = 0;
        member.clear();
        create_date = 0;
        gid = 0;
    }
}
