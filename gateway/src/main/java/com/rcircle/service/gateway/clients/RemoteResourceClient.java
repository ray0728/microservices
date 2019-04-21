package com.rcircle.service.gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "service-resource", configuration = RemoteRequestWithTokenConfiguration.class)
public interface RemoteResourceClient {
    @GetMapping("/cate/list")
    public String getAllCategorys();

    @PostMapping("/cate/new")
    public String addNewCategory(@RequestParam(name = "desc", required = true) String desc);

    @GetMapping("/res/list")
    public String getAllDiaries(@RequestParam(name = "type", required = false, defaultValue = "0") int type,
                              @RequestParam(name = "gid", required = false, defaultValue = "0") int gid);
}
