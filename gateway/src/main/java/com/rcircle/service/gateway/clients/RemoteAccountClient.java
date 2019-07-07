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

    @RequestMapping(method = RequestMethod.POST, value = "/account/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Headers("Content-Type: multipart/form-data")
    public String create(@RequestPart(value = "file") MultipartFile file,
                         @RequestParam(name = "usrname") String username,
                         @RequestParam(name = "email") String email,
                         @RequestParam(name = "passwd") String password,
                         @RequestParam(name = "signature") String profile,
                         @RequestParam(name = "resume") String resume,
                         @RequestParam(name = "checksum") String checksum);

    @RequestMapping(method = RequestMethod.PUT, value = "/account/refresh")
    public String refreshTime();
}
