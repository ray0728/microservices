package com.rcircle.service.gateway.services;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.gateway.clients.RemoteSsoClient;
import com.rcircle.service.gateway.clients.RemoteSsoRequestInterceptor;
import com.rcircle.service.gateway.utils.HttpContextHolder;
import com.rcircle.service.gateway.utils.Toolkit;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OAuth2SsoService {

    @Value("${security.oauth2.client.client-id}")
    private String clientid;
    @Value("${security.oauth2.client.client-secret}")
    private String secret;

    @Value("${server.port}")
    private int port;

    @Autowired
    private RemoteSsoClient remoteSsoClient;

    @HystrixCommand(fallbackMethod = "buildFallbackAccessToken", threadPoolKey = "AccessTokenThreadPool")
    public String getAccessToken(String username, String password) {
        HttpContextHolder.getContext().setValue(RemoteSsoRequestInterceptor.USERNAMEANDPASSWORD, username + ":" + password);
        String state = Toolkit.randomString(8);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("response_type", "code");
        parameters.put("client_id", clientid);
        parameters.put("redirect_uri", extractRedirect("/rst/redirect"));
        parameters.put("state", state);
        String map = remoteSsoClient.getAuthorizeCode(parameters);
        HashMap<String, String> authcode = JSON.parseObject(map, HashMap.class);
        if (!state.equals(authcode.get("state"))) {
            return "failed! state mismatch";
        }
        HttpContextHolder.getContext().delete(RemoteSsoRequestInterceptor.USERNAMEANDPASSWORD);
        parameters.remove("response");
        parameters.put("client_secret", secret);
        parameters.put("grant_type", "authorization_code");
        parameters.put("code", authcode.get("code"));
        String token = remoteSsoClient.getAccessToken(parameters);
        return token;
    }

    public String buildFallbackAccessToken(String username, String password, Throwable throwable) {
        int status = 0;
        String reason = null;
        if (throwable instanceof FeignException) {
            status = ((FeignException) throwable).status();
        }
        switch (status) {
            case 401:
                reason = "Incorrect Password!";
                break;
            case 500:
                reason = "Server Busy";
                break;
            default:
                reason ="Network Timeout";
                break;
        }
        return "failed! " + reason;
    }

    private String extractRedirect(String endpoint) {
        return String.format("http://%s:%d%s", Toolkit.getLocalIP(), port, endpoint);
    }
}
