package com.rcircle.service.gateway.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

public class Account implements Authentication, CredentialsContainer {
    private String email;
    private Object credentials;
    private Object principal;
    private Object detials;
    private String profile;
    private JWTToken jwtToken = null;
    private boolean authenticated = false;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return detials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        authenticated = b;
    }

    @Override
    public String getName() {
        if (principal instanceof String) {
            return (String) principal;
        }
        return principal.toString();
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    public void setToken(String token) {
        jwtToken = JSONObject.parseObject(token, JWTToken.class);
    }

    public JWTToken getToken(){
        return jwtToken;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    public Object getDetials() {
        return detials;
    }

    public void setDetials(Object detials) {
        this.detials = detials;
    }

    @Override
    public void eraseCredentials() {
        credentials = null;
    }
}
