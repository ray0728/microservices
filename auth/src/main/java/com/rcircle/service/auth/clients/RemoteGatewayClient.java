package com.rcircle.service.auth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="service-gateway")
public interface RemoteGatewayClient {
    @RequestMapping(method = RequestMethod.GET, value="/rst/ai")
    public String ai(@RequestParam(name="type")int type);
}
