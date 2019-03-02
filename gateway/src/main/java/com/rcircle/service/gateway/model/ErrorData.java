package com.rcircle.service.gateway.model;

import java.io.Serializable;

public class ErrorData implements Serializable {
    public static final int INVALID_CODE = 0;
    public int code = INVALID_CODE;
    public String type;
    public String msg;

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

    public String toString() {
        return String.format("failed![%d][%s]%s", code, type, msg);
    }
}