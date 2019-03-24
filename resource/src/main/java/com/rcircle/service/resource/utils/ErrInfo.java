package com.rcircle.service.resource.utils;

import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.resource.model.ErrorData;


public class ErrInfo {
    public static final int CODE_CREATE_NEW = 501;
    public static final int CODE_UPDATE_RES = 502;
    public static final int CODE_DELETE_RES = 503;
    public static final int CODE_DELETE_RES_FILES = 504;
    public static final int CODE_UPLOAD_RES = 505;
    public static final int CODE_GET_RES_FILES = 506;
    public static final int CODE_CREATE_REPLY = 507;
    public static final int CODE_DELETE_REPLY = 508;
    public static final int CODE_GET_RES = 509;
    public static final int CODE_CREATE_FILE_STREAM = 510;
    public static final int CODE_SAVE_NETFILE = 511;
    public static final int CODE_GET_ALL_CATEGORY_FOR_ACCOUNT = 512;
    public static final int CODE_CREATE_NEW_CATEGORY = 513;
    public static final int CODE_DELETE_CATEGORY = 514;
    public static final int CODE_UPDATE_CATEGORY = 515;


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
