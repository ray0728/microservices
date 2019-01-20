package com.ray.service.store.util;

import com.alibaba.fastjson.JSONObject;
import com.ray.service.store.model.ErrorData;


public class ErrInfo {
    public static final int CODE_GET_ACCOUNT = 501;
    public static final int CODE_SAVE_NETFILE = 502;
    public static final int CODE_GET_LOGFILE = 503;
    public static final int CODE_UPDATE_LOGFILE = 504;
    public static final int CODE_CREATE_FILE_STREAM = 505;
    public static final int CODE_GET_FILES = 506;


    public static enum ErrType {
        SUCCESS,
        PARAMS,
        NULLOBJ,
        INVALID,
        MISMATCH
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
            case INVALID:
                desc = "INVALID";
                break;
            case MISMATCH:
                desc = "MISMATCH";
                break;
            case SUCCESS:
                desc = "SUCCESS";
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
