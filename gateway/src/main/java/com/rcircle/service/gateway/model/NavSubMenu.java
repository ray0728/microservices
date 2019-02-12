package com.rcircle.service.gateway.model;

import java.io.Serializable;

public class NavSubMenu implements Serializable {
    private String name;
    private String url;

    public NavSubMenu(String name, String url){
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDivider(){
        return name == null;
    }
}
