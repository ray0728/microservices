package com.rcircle.service.account.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    private static final long serialVersionUID = 201810011523001L;
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_EXPIRED = 1;
    public static final int STATUS_LOCKED = 2;
    public static final int STATUS_DISABLED = 3;
    private int uid = 0;
    private String username;
    private String password;
    private String email;
    private int status = STATUS_NORMAL;
    private long firsttime;
    private int times;
    private long lastlogin;
    private List<Authority> roles;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if(!roles.contains(auth)) {
            roles.add(auth);
        }
    }

    public void deleteRole(int rid){
        Authority auth = new Authority();
        auth.setId(rid);
        if(roles != null && roles.contains(auth)){
            roles.remove(auth);
        }
    }

    public List<Authority> getRoles() {
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

    public int getMaxLevelRole(){
        int rid = Authority.ID_GUEST;
        for (Authority role : roles) {
            if(role.getId() > rid){
                rid = role.getId();
            }
        }
        return rid;
    }

    public int getMinLevelRole(){
        int rid = Authority.ID_SUPER;
        for (Authority role : roles) {
            if(role.getId() < rid){
                rid = role.getId();
            }
        }
        return rid;
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
    }
}
