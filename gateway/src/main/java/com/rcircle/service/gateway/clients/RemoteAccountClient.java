package com.rcircle.service.gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name="service-account", configuration = RemoteRequestWithTokenConfiguration.class)
public interface RemoteAccountClient {
    @RequestMapping(method = RequestMethod.GET, value = "/account/info")
    public String getInfo(@RequestParam(name = "username") String username, @RequestParam(name = "uid") int id);

    @RequestMapping(method = RequestMethod.POST, value = "/account/create")
    public String create(@RequestParam(name = "usrname") String username,
                         @RequestParam(name = "email") String email,
                         @RequestParam(name = "passwd") String password,
                         @RequestParam(name = "roles") int[] roles,
                         @RequestParam(name = "profile") String profile);

    @RequestMapping(method = RequestMethod.PUT, value = "/account/refresh")
    public String refreshTime();
}
