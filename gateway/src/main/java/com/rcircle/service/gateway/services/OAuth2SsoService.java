package com.rcircle.service.gateway.services;

import com.rcircle.service.gateway.clients.RemoteSsoClient;
import com.rcircle.service.gateway.clients.RemoteSsoRequestInterceptor;
import com.rcircle.service.gateway.utils.HttpContextHolder;
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
    @Value("${security.oauth2.client.client-id}")
    private String auth_url;
    @Value("${security.oauth2.client.client-id}")
    private String access_token_url;

    @Autowired
    private RemoteSsoClient remoteSsoClient;

    public int getAuthorizeCode(String username, String password) {
        HttpContextHolder.getContext().setValue(RemoteSsoRequestInterceptor.USERNAMEANDPASSWORD, username + ":" + password);
        Map<String, String> parameters = new HashMap<>();

        remoteSsoClient.getAuthorizeCode();
    }
}
