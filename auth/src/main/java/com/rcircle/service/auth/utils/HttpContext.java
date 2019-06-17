package com.rcircle.service.auth.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HttpContext {
    public static final String AUTH_TOKEN = "authorization";
    public static final String QUERY_ACCOUNT_SECU = "rc-account-security";

    private Map<String, String> contextMap = new HashMap<>();

    public void setValue(String key, String value) {
        contextMap.put(key, value);
    }

    public String getValue(String key) {
        return contextMap.get(key);
    }

    public void clear(){
        contextMap.clear();
    }
}
