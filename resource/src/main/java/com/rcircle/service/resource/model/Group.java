package com.rcircle.service.resource.model;

import java.io.Serializable;

public class Group implements Serializable {
    private int gid = 0;
    private String name = "";
    private String desc = "";
    private int type = 0;
    private int level = 0;
    private int admin_uid = 0;
    private long create_date = 0;

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
}
