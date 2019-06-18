package com.rcircle.service.stream.Controller;

import com.rcircle.service.stream.services.HLSService;
import com.rcircle.service.stream.utils.Toolkit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/stream")
public class StreamController {
    private static final int TYPE_CREATE_HLS = 0;
    @Resource
    private HLSService hlsService;

    @PostMapping("/create")
    public String createHLSFiles(@RequestParam("src") String srcfile,
                                 @RequestParam("dst") String dstpath,
                                 @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                 @RequestParam(value = "id", required = false, defaultValue = "0") int logid,
                                 @RequestParam(value = "url", required = false, defaultValue = "") String baseurl) {
        switch (type) {
            case TYPE_CREATE_HLS:
                srcfile = Toolkit.decodeFromUrl(srcfile);
                dstpath = Toolkit.decodeFromUrl(dstpath);
                baseurl = Toolkit.decodeFromUrl(baseurl);
                hlsService.createHLSFiles(logid, srcfile, dstpath, baseurl);
                break;
        }
        return "";
    }
}
