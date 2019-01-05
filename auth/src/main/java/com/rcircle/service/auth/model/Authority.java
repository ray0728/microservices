package com.rcircle.service.auth.model;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
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
