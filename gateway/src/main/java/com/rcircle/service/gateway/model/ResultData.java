package com.rcircle.service.gateway.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResultData implements Serializable {
    public int code;
    public String type;
    public String msg;
    public Map<String, Object> map;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public void addToMap(String key, Object obj) {
        if (map == null) {
            map = new HashMap<>();
        }
        map.putIfAbsent(key, obj);
    }

    public boolean isSuccess(){
        return type.equalsIgnoreCase("SUCCESS");
    }

    public String toString() {
        if (isSuccess()) {
            return String.format("success![%d]%s", code, msg);
        } else {
            return String.format("failed![%d][%s]%s", code, type, msg);
        }
    }
}
