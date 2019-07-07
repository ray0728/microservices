package com.rcircle.service.gateway.clients;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(name = "service-account", configuration = RemoteRequestWithTokenConfiguration.class)
public interface RemoteAccountClient {
    @RequestMapping(method = RequestMethod.GET, value = "/account/info")
    public String getInfo(@RequestParam(name = "username") String username, @RequestParam(name = "uid") int id);


    @RequestMapping(method = RequestMethod.PUT, value = "/account/refresh")
    public String refreshTime();
}
