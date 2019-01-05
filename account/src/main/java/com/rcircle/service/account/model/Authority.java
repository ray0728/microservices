package com.rcircle.service.account.model;

import java.io.Serializable;

public class Authority implements Serializable {
    public static final String ROLE_GUEST = "GUEST";
    public static final int ID_GUEST = 1;
    public static final String ROLE_USER = "USER";
    public static final int ID_USER = 2;
    public static final String ROLE_ADMIN = "ADMIN";
    public static final int ID_ADMIN = 7;
    public static final String ROLE_SUPER = "SUPER";
    public static final int ID_SUPER = 9;
    private int id = 0;
    private String authority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                this.id = ID_GUEST;
                break;
        }
        return "ROLE_" + desc;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Authority)) {
            return false;
        }
        return getId() == ((Authority) obj).getId();
    }

    public boolean isAdminRole() {
        return  authority.replace("ROLE_", "").equals(ROLE_ADMIN);
    }

    public boolean isSuperRole(){
        return  authority.replace("ROLE_", "").equals(ROLE_SUPER);
    }
}
