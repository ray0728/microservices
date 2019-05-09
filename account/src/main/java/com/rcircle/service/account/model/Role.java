package com.rcircle.service.account.model;

import java.io.Serializable;

public class Role implements Serializable {
    public static final String ROLE_GUEST = "GUEST";
    public static final int ID_GUEST = 1;
    public static final String ROLE_USER = "USER";
    public static final int ID_USER = 2;
    public static final String ROLE_ADMIN = "ADMIN";
    public static final int ID_ADMIN = 7;
    public static final String ROLE_SUPER = "SUPER";
    public static final int ID_SUPER = 9;
    private int rid = 0;
    private int mid;
    private String authority;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int id) {
        this.rid = id;
        setAuthority(translate(id));
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    private String translate(int id) {
        String desc = null;
        switch (id) {
            case ID_GUEST:
                desc = ROLE_GUEST;
                break;
            case ID_USER:
                desc = ROLE_USER;
                break;
            case ID_ADMIN:
                desc = ROLE_ADMIN;
                break;
            case ID_SUPER:
                desc = ROLE_SUPER;
                break;
            default:
                desc = ROLE_GUEST;
                this.rid = ID_GUEST;
                break;
        }
        return "ROLE_" + desc;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Role)) {
            return false;
        }
        return getRid() == ((Role) obj).getRid();
    }

    public boolean isAdminRole() {
        return  authority.replace("ROLE_", "").equals(ROLE_ADMIN);
    }

    public boolean isSuperRole(){
        return  authority.replace("ROLE_", "").equals(ROLE_SUPER);
    }
}
