package com.rcircle.service.gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service-resource", configuration = RemoteRequestWithTokenConfiguration.class)
public interface RemoteResourceClient {
    @GetMapping("/cate/list")
    public String getAllCategorys();
}
