package com.rcircle.service.stream.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-message")
public interface RemoteMessageFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "/hls/split/result")
    public String sendHLSSplitFinished(@RequestParam(name = "id") int logid,
                                       @RequestParam(name="file") String filename,
                                       @RequestParam(name="result") boolean ret);
}
