package com.rcircle.service.gateway.model;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class Role implements Serializable, GrantedAuthority {
    public static final String ROLE_GUEST = "GUEST";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_SUPER = "SUPER";
    private String authority;

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public boolean isAdminRole() {
        return  authority.replace("ROLE_", "").equals(ROLE_ADMIN);
    }

    public boolean isSuperRole(){
        return  authority.replace("ROLE_", "").equals(ROLE_SUPER);
    }
}
