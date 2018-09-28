package com.ray.service.account.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Account implements Serializable {
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_EXPIRED = 1;
    public static final int STATUS_LOCKED = 2;
    public static final int STATUS_DISABLED = 3;
    private int uid = 0;
    private String username;
    private String password;
    private int status = STATUS_NORMAL;
    private long firsttime;
    private int times;
    private long lastlogin;
    private List<GrantedAuthority> roles;
    private User userDetails;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(long lastlogin) {
        this.lastlogin = lastlogin;
    }

    public void addRole(int rid) {
        Authority auth = new Authority();
        auth.setId(rid);
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(auth);
    }

    public List<GrantedAuthority> getRoles() {
        return roles;
    }

    public long getFirsttime() {
        return firsttime;
    }

    public void setFirsttime(long firsttime) {
        this.firsttime = firsttime;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void appendOneTimes() {
        this.times++;
    }

    public User translat() {
        if (userDetails == null) {
            userDetails = new User(username, password, roles);
        }
        return userDetails;
    }

    public void reset() {
        uid = 0;
        username = null;
        password = null;
        status = STATUS_NORMAL;
        firsttime = 0;
        times = 0;
        lastlogin = 0;
        roles.clear();
        roles = null;
        userDetails = null;
    }
}
