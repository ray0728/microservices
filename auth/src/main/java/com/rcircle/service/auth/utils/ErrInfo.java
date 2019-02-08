package com.rcircle.service.auth.utils;

import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.auth.model.ErrorData;


public class ErrInfo {
    public static final int CODE_GET_ACCOUNT = 401;
    public static enum ErrType {
        PARAMS,
        NULLOBJ
    };

    private static String translate(ErrType type) {
        String desc = null;
        switch (type) {
            case PARAMS:
                desc = "PARAMS";
                break;
            case NULLOBJ:
                desc = "NULLOBJ";
                break;
            default:
                desc = "UNKNOWN";
                break;
        }
        return desc;
    }

    public static String assembleJson(ErrType type, int code, String msg) {
        ErrorData data = new ErrorData();
        data.setCode(code);
        data.setType(translate(type));
        data.setMsg(msg);
        return JSONObject.toJSONString(data);
    }
}
