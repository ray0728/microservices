package com.rcircle.service.gateway.filters;

import com.rcircle.service.gateway.clients.RemoteRequestWithTokenInterceptor;
import com.rcircle.service.gateway.model.Account;
import com.rcircle.service.gateway.utils.HttpContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;

@Component
public class HttpContextFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(!HttpContextHolder.getContext().isEmpty()){
            HttpContextHolder.getContext().clear();
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Enumeration<String> headers = httpServletRequest.getHeaderNames();
        String header = null;
        while(headers.hasMoreElements()){
            header = headers.nextElement();
            HttpContextHolder.getContext().setValue(header, httpServletRequest.getHeader(header));
            Principal principal = httpServletRequest.getUserPrincipal();
            if(principal != null && principal instanceof Account){
                if(((Account) principal).isAuthenticated()) {
                    HttpContextHolder.getContext().setValue(RemoteRequestWithTokenInterceptor.ACCESSTOKEN, ((Account) principal).getToken().getAccess_token());
                }
            }
        }
        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
