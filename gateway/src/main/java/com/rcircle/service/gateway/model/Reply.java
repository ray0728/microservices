package com.rcircle.service.gateway.model;

import com.rcircle.service.gateway.utils.Toolkit;

import java.io.Serializable;

public class Reply  implements Serializable {
    private int id;
    private int lid;
    private int uid;
    private String username;
    private String email;
    private String desc;
    private long date;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMonth() {
        return Toolkit.getMonthFrom(date);
    }

    public int getYear() {
        return Toolkit.getYearFom(date);
    }

    public int getDay() {
        return Toolkit.getDayFrom(date);
    }
}
