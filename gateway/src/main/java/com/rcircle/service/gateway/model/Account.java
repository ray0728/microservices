package com.rcircle.service.gateway.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Account implements Authentication, CredentialsContainer {
    private int uid;
    private String email;
    private Object credentials;
    private Object principal;
    private Object detials;
    private String signature;
    private String resume;
    private String avatar;
    private JWTToken jwtToken = null;
    private boolean authenticated = false;
    private String errinfo;
    private long lastlogin;
    private List<Role> roles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResume() {
        return resume;
    }

    public String autoDetectResume() {
        return resume != null && !resume.isEmpty() ? resume : signature;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar.equals("1") ?  "/api/user/account/avatar/" + uid : "/global/img/default-avatar.png";
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
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
        if (principal instanceof Principal) {
            return ((Principal) principal).getName();
        }
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

    public JWTToken getToken() {
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

    public String getErrinfo() {
        return errinfo;
    }

    public void setErrinfo(String errinfo) {
        this.errinfo = errinfo;
    }

    public boolean hasError() {
        return errinfo != null && !errinfo.isEmpty();
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(long lastlogin) {
        this.lastlogin = lastlogin;
    }

    public void setUsername(String name) {
        principal = name;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void copyFrom(Account account) {
        if (!account.hasError()) {
            setEmail(account.getEmail());
            setLastlogin(account.getLastlogin());
            setSignature(account.getSignature());
            setResume(account.getResume());
            setUsername(account.getName());
            Iterator<? extends GrantedAuthority> iter = account.getAuthorities().iterator();
            List<Role> roles = new ArrayList<>();
            Role role;
            while (iter.hasNext()) {
                role = new Role();
                role.setAuthority(iter.next().getAuthority());
                roles.add(role);
            }
            setRoles(roles);
        }
    }
}
