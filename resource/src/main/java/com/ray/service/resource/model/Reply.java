package com.ray.service.resource.model;

import java.io.Serializable;

public class Reply implements Serializable {
    private int id;
    private int lid;
    private int uid;
    private String desc;
    private long date;
    private int like_num;
    private int unlike_num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public int getUnlike_num() {
        return unlike_num;
    }

    public void setUnlike_num(int unlike_num) {
        this.unlike_num = unlike_num;
    }
}
