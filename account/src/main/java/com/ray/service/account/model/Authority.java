package com.ray.service.account.model;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
    private static final long serialVersionUID = 201810011526002L;
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
        setAuthority(translate(id));
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    private String translate(int id) {
        String desc = null;
        switch (id) {
            case 1:
                desc = ROLE_GUEST;
                break;
            case 2:
                desc = ROLE_BEGINNER;
                break;
            case 3:
                desc = ROLE_SKILLED;
                break;
            case 4:
                desc = ROLE_EXPERT;
                break;
            case 5:
                desc = ROLE_AUTHORITY;
                break;
            case 6:
                desc = ROLE_ADMIN;
                break;
            case 7:
                desc = ROLE_DEBUG;
                break;
            default:
                desc = ROLE_GUEST;
                break;
        }
        return "ROLE_" + desc;
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
