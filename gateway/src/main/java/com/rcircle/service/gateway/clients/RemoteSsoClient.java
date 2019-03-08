package com.rcircle.service.gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@FeignClient(name = "service-auth", configuration = RemoteSsoClientConfiguration.class)
public interface RemoteSsoClient {
    @RequestMapping({"/oauth/authorize"})
    @ResponseBody
    public String getAuthorizeCode(@RequestParam Map<String, String> parameters);

    @RequestMapping(
            value = {"/oauth/token"},
            method = {RequestMethod.POST}
    )
    public String getAccessToken(@RequestParam Map<String, String> parameters);

    @RequestMapping({"/oauth/check_token"})
    @ResponseBody
    public String checkToken(@RequestParam("token") String value);
}
