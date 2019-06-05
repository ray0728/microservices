package com.rcircle.service.gateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.rcircle.service.gateway.clients.RemoteRequestWithTokenInterceptor;
import com.rcircle.service.gateway.utils.HttpContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class DetectTokenFilter extends ZuulFilter {
    private static final String PRE_FILTER_TYPE = "pre";
//    public static final String ROUTE_FILTER_TYPE = "route";
    private static final int FILTER_ORDER = 0;
    private static final String BEARER = "Bearer";
    private static final String AUTHORIZATION = "Authorization";

    @Override
    public String filterType() {
        return PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        boolean hasAuthorization = false;
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getHeader(AUTHORIZATION) != null
                || ctx.getZuulRequestHeaders().get(AUTHORIZATION) != null) {
            hasAuthorization = true;
        } else if (HttpContextHolder.getContext().getValue(RemoteRequestWithTokenInterceptor.ACCESSTOKEN) != null) {
            ctx.addZuulRequestHeader(AUTHORIZATION, extractToken((String) HttpContextHolder.getContext().getValue(RemoteRequestWithTokenInterceptor.ACCESSTOKEN)));
            hasAuthorization = true;
        }
        if (!hasAuthorization) {
            ctx.set("error.status_code", HttpServletResponse.SC_UNAUTHORIZED);
            ctx.set("error.message", "You need Login first");
            ctx.set("error.exception", new Exception("UNAUTHORIZED"));
        }
        return null;
    }

    private String extractToken(String token) {
        return String.format("%s %s", BEARER, token);
    }
}
