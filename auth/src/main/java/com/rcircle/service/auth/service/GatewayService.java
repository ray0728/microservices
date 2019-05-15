package com.rcircle.service.auth.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.auth.clients.RemoteGatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {
    @Autowired
    private RemoteGatewayClient remoteGatewayClient;

    @HystrixCommand(fallbackMethod = "buildFallbackGetGatewayIpAndPort", threadPoolKey = "GatewayThreadPool")
    public String getGatewayIpAndPort(){
        return remoteGatewayClient.ai(2);
    }

    public String buildFallbackGetGatewayIpAndPort(Throwable throwable){
        return "";
    }
}
