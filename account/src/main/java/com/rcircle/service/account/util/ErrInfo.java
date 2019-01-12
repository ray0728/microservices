package com.rcircle.service.account.util;

import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.account.model.ErrorData;


public class ErrInfo {
    public static final int CODE_CREATE_ACCOUNT = 301;
    public static final int CODE_DELETE_ACCOUNT = 302;
    public static final int CODE_CHANGE_ACCOUNT = 303;
    public static final int CODE_REFRESH_ACCOUNT = 304;
    public static final int CODE_EDIT_ACCOUNT = 305;
    public static final int CODE_CREATE_GROUP = 306;
    public static final int CODE_DELETE_GROUP = 307;
    public static final int CODE_CHANGE_GROUP = 308;
    public static final int CODE_CHANGE_GROUP_MEMBER = 309;
    public static final int CODE_QUERY_GROUP= 310;

    public static enum ErrType {
        PARAMS,
        NULLOBJ,
        INVALID
    }

    ;

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
