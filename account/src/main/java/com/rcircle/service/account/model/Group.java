package com.rcircle.service.account.model;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {
    private int gid;
    private String name;
    private String desc;
    private int type;
    private int level;
    private int admin_uid;
    private long create_date;
    private List<Account> member;

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

    public List<Account> getMember() {
        return member;
    }

    public void setMember(List<Account> member) {
        this.member = member;
    }
}
