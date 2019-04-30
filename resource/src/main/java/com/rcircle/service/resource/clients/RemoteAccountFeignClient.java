package com.rcircle.service.resource.clients;

import com.rcircle.service.resource.utils.HttpContextInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-account", configuration = HttpContextInterceptor.class)
public interface RemoteAccountFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/account/info")
    public String getInfo(@RequestParam(name = "username") String username, @RequestParam(name = "uid") int uid);

    @RequestMapping(method = RequestMethod.GET, value = "/group/info")
    public String getGroupsInfo();

}
