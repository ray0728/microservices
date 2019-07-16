package com.rcircle.service.auth.security.endpoint;

import com.rcircle.service.auth.service.GatewayService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;

import javax.annotation.Resource;

public class LocalRedirectResolver implements RedirectResolver {
    private GatewayService gatewayService;
    public LocalRedirectResolver setGatewayService(GatewayService service){
        gatewayService = service;
        return this;
    }
    public String resolveRedirect(String requestedRedirect, ClientDetails client) throws OAuth2Exception {
        String gw = gatewayService.getGatewayIpAndPort();
        if(requestedRedirect.substring(requestedRedirect.indexOf("//") + 2).equals(gw+"/rst/redirect")) {
            return requestedRedirect;
        }else{
            throw new RedirectMismatchException("Invalid redirect: " + requestedRedirect + " does not match one of the registered values.[" + gw + "/rst/redirect]");
        }
    }
}
