package com.rcircle.service.gateway.model;

import java.io.Serializable;

public class Tag implements Serializable {
    private int id;
    private int mid;
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}
