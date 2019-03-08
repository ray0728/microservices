package com.rcircle.service.gateway.clients;

import com.rcircle.service.gateway.utils.HttpContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Util;

import com.rcircle.service.gateway.utils.Base64;

import java.util.Set;

public class RemoteSsoRequestInterceptor implements RequestInterceptor {
    private static final String BEARER = "Bearer";
    private static final String BASIC = "Basic";
    private static final String AUTHORIZATION = "Authorization";
    public static final String USERNAMEANDPASSWORD = "usernameandpassword";
    public static final String ACCESSTOKEN = "accesstoken";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Set<String> keys = HttpContextHolder.getContext().keys();
        for (String key : keys) {
            if (key.equals(USERNAMEANDPASSWORD)) {
                requestTemplate.header(AUTHORIZATION, new String[]{extractBasic(HttpContextHolder.getContext().getValue(key).toString())});
            } else if (key.equals(ACCESSTOKEN)) {
                requestTemplate.header(AUTHORIZATION, new String[]{extractToken(HttpContextHolder.getContext().getValue(key).toString())});
            } else {
                requestTemplate.header(key, HttpContextHolder.getContext().getStringArrayValue(key));
            }
        }
    }

    protected String extractToken(String token) {
        return String.format("%s %s", BEARER, token);
    }

    protected String extractBasic(String basicauthinfo) {
        return String.format("%s %s", BASIC, base64Encode(basicauthinfo.getBytes(Util.ISO_8859_1)));
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.encode(bytes);
    }
}
