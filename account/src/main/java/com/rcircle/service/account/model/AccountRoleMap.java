package com.rcircle.service.account.model;

import java.io.Serializable;

public class AccountRoleMap implements Serializable {
    private static final long serialVersionUID = 2018100115270031L;
    private int id;
    private int uid;
    private int rid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }
}
