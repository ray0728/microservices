package com.rcircle.service.gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="service-auth", configuration = RemoteSsoClientConfiguration.class)
public interface RemoteSsoClient {
    @RequestMapping(method = RequestMethod.GET, value = "/sso/oauth/authorize")
    public String getAuthorizationCode(String response_type, String client_id, String state, String redirect_uri);

    public String getAccessToken();
}
