package com.ray.service.store.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="service-account", fallback= RemoteAccountFeignClientHystrix.class)
public interface RemoteAccountFeignClient {
    @RequestMapping(method = RequestMethod.GET, value="/account/info")
    public String getInfo(@RequestParam(name = "username", required = true) String username);

}
