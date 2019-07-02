package com.rcircle.service.account.util;

import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.account.model.ResultData;


public class ResultInfo {
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
    public static final int CODE_SAVE_FILE = 311;
    public static final int CODE_OPEN_FILE = 312;
    public static final int CODE_UPLOAD_AVATAR = 313;
    public static final int CODE_CHECK_SUM = 314;

    public static enum ErrType {
        SUCCESS,
        PARAMS,
        NULLOBJ,
        INVALID,
        EXCEPTION
    }

    public static String translate(ErrType type) {
        String desc = null;
        switch (type) {
            case PARAMS:
                desc = "PARAMS";
                break;
            case NULLOBJ:
                desc = "NULLOBJ";
            case SUCCESS:
                desc = "SUCCESS";
                break;
            case EXCEPTION:
                desc = "EXCEPTION";
                break;
            default:
                desc = "UNKNOWN";
                break;
        }
        return desc;
    }

    public static String assembleJson(ErrType type, int code, String msg) {
        ResultData data = new ResultData();
        data.setCode(code);
        data.setType(translate(type));
        data.setMsg(msg);
        return JSONObject.toJSONString(data);
    }

    public static String assembleSuccessJson(int code, String msg, String label, Object obj) {
        ResultData data = new ResultData();
        data.setCode(code);
        data.setType(translate(ErrType.SUCCESS));
        if (msg != null) {
            data.setMsg(msg);
        }
        if(label != null) {
            data.addToMap(label, obj);
        }
        return JSONObject.toJSONString(data);
    }
}
