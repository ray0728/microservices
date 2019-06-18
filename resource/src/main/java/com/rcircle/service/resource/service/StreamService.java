package com.rcircle.service.resource.service;

import com.rcircle.service.resource.clients.RemoteStreamFeignClient;
import com.rcircle.service.resource.utils.NetFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamService {
    @Autowired
    private RemoteStreamFeignClient remoteStreamFeignClient;

    public void asynCreateHLSFiles(int logid, String srcfile, String dstpath, String baseurl){
        srcfile = NetFile.translateToUrlFormat(srcfile);
        dstpath = NetFile.translateToUrlFormat(dstpath);
        baseurl = NetFile.translateToUrlFormat(baseurl);
        remoteStreamFeignClient.createHLSFiles(srcfile, dstpath, logid, baseurl);
    }
}
