package com.ray.service.account.util;

import com.alibaba.fastjson.JSONObject;
import com.ray.service.account.model.ErrorData;


public class ErrInfo {
    public static final int CODE_CREATE_ACCOUNT = 401;
    public static final int CODE_DELETE_ACCOUNT = 402;
    public static final int CODE_CHANGE_ACCOUNT = 403;
    public static final int CODE_REFRESH_ACCOUNT = 404;
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
