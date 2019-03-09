package com.rcircle.service.gateway.clients;

import com.rcircle.service.gateway.utils.HttpContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Util;

import com.rcircle.service.gateway.utils.Base64;

import java.util.Map;

public class RemoteSsoRequestInterceptor implements RequestInterceptor {
    private static final String BEARER = "Bearer";
    private static final String BASIC = "Basic";
    private static final String AUTHORIZATION = "Authorization";
    public static final String USERNAMEANDPASSWORD = "usernameandpassword";
    public static final String ACCESSTOKEN = "accesstoken";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, Object> map = HttpContextHolder.getContext().getMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            switch (entry.getKey()) {
                case USERNAMEANDPASSWORD:
                    requestTemplate.header(AUTHORIZATION, new String[]{extractBasic((String) entry.getValue())});
                    break;
                case ACCESSTOKEN:
                    requestTemplate.header(AUTHORIZATION, new String[]{extractToken((String) entry.getValue())});
                    break;
                default:
                    requestTemplate.header(entry.getKey(), (String) entry.getValue());
                    break;
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
