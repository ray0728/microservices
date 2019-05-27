package com.rcircle.service.resource.clients;

import com.rcircle.service.resource.utils.HttpContextInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-stream", configuration = HttpContextInterceptor.class)
public interface RemoteStreamFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "/stream/create")
    public String createHLSFiles(@RequestParam("src") String srcfile,
                                 @RequestParam("dst") String dstpath,
                                 @RequestParam(value = "id") int logid,
                                 @RequestParam(value = "url") String baseurl);
}
