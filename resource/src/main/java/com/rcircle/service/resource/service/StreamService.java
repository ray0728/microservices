package com.rcircle.service.resource.service;

import com.rcircle.service.resource.clients.RemoteStreamFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamService {
    @Autowired
    private RemoteStreamFeignClient remoteStreamFeignClient;

    public void asynCreateHLSFiles(int logid, String srcfile, String dstpath, String baseurl){
        remoteStreamFeignClient.createHLSFiles(srcfile, dstpath, logid, baseurl);
    }
}
