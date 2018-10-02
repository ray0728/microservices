package com.ray.service.auth.model;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
    private static final long serialVersionUID = 201810011526002L;
    public static final String ROLE_GUEST = "Guest";
    public static final String ROLE_BEGINNER = "Beginner";
    public static final String ROLE_SKILLED = "Skilled";
    public static final String ROLE_EXPERT = "Expert";
    public static final String ROLE_AUTHORITY = "Authority";
    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_DEBUG = "Debug";
    private int id = 0;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        setDescription(getDescription(id));
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription(int id) {
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
                desc = description == null ? ROLE_GUEST : description;
                break;
        }
        return desc;
    }

    public void setAuthority(String authority){
        description = authority;
    }
    @Override
    public String getAuthority() {
        return description;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Authority)) {
            return false;
        }
        return getId() == ((Authority) obj).getId();
    }
}
