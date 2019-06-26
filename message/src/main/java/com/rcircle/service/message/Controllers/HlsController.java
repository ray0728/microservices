package com.rcircle.service.message.Controllers;

import com.rcircle.service.message.services.HlsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/hls")
public class HlsController {
    @Resource
    private HlsService hlsService;

    @PostMapping("/split/result")
    public String sendHLSSplitFinished(@RequestParam(name = "id") int logid,
                                       @RequestParam(name="file") String filename,
                                       @RequestParam(name="result") boolean ret){
        hlsService.sendHLSSplitFinished(logid, filename, ret);
        return "";
    }
}
