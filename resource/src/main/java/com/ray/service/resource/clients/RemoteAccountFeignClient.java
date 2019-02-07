package com.ray.service.resource.clients;

import com.ray.service.resource.utils.HttpContextInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-account", configuration = HttpContextInterceptor.class)
public interface RemoteAccountFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/account/info")
    public String getInfo(@RequestParam(name = "username", required = true) String username);

    @RequestMapping(method = RequestMethod.GET, value = "/group/info")
    public String getGroupsInfo();

}
