package com.rcircle.service.gateway.model;

import java.io.Serializable;

public class Category implements Serializable {
    private int cid;
    private String desc;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
