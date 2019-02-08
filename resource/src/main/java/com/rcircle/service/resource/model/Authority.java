package com.rcircle.service.resource.model;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
    public static final String ROLE_GUEST = "GUEST";
    public static final String ROLE_BEGINNER = "BEGINNER";
    public static final String ROLE_SKILLED = "SKILLED";
    public static final String ROLE_EXPERT = "EXPERT";
    public static final String ROLE_AUTHORITY = "AUTHORITY";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_DEBUG = "DEBUG";
    private int id = 0;
    private String authority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Authority)) {
            return false;
        }
        return getId() == ((Authority) obj).getId();
    }
}