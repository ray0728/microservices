package com.rcircle.service.gateway.filters;

import brave.Tracer;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TracerFilter extends ZuulFilter {
    public static final String POST_FILTER_TYPE = "post";
    private static final int FILTER_ORDER = 1;

    @Autowired
    private Tracer tracer;

    @Override
    public String filterType() {
        return POST_FILTER_TYPE;
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
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.getResponse().addHeader("sl-correlation-id", tracer.currentSpan().context().traceIdString());
        return null;
    }
}
