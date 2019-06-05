package com.rcircle.service.gateway.security.authentication;

import com.rcircle.service.gateway.utils.Base64;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuthAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        String errorinfo = exception.getMessage().toLowerCase();
        if(errorinfo.contains("incorrect")){
            errorinfo = "Incorrect User name or Password";
        }else{
            errorinfo = "Network Timeout";
        }
        response.sendRedirect("/login?info=" + Base64.encode(errorinfo.getBytes()));
        return;
    }
}
