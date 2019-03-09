package com.rcircle.service.gateway.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

public class Account implements Authentication, CredentialsContainer {
    private String email;
    private String accesstoken;

    private Object credentials;
    private Object principal;
    private Object detials;
    private boolean authenticated = false;


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

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
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
