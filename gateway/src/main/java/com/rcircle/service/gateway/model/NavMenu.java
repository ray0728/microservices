package com.rcircle.service.gateway.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class NavMenu implements Serializable {
    public static final int STATE_IDLE = 0;
    public static final int STATE_ACTIVE = 1;
    public static final int TYPE_BUTTON = 0;
    public static final int TYPE_DROPDOWN = 1;
    private String name;
    private String url;
    private int state = STATE_IDLE;
    private int type = TYPE_BUTTON;

    private List<NavSubMenu> dropList;

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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isActive() {
        return state == STATE_ACTIVE;
    }

    public boolean isDropDownMenu() {
        return type == TYPE_DROPDOWN;
    }

    public void addSubItem(String name, String url){
        if(dropList == null){
            dropList = new LinkedList<>();
        }
        dropList.add(new NavSubMenu(name, url));
    }

    public List<NavSubMenu> getDropList() {
        return dropList;
    }
}
