package com.rcircle.service.gateway.utils;

import org.springframework.util.Assert;

public class HttpContextHolder {
    private static final ThreadLocal<HttpContext> httpContextThreadLocal = new ThreadLocal<HttpContext>();

    public static final HttpContext getContext() {
        HttpContext httpContext = httpContextThreadLocal.get();
        if (httpContext == null) {
            httpContext = createHttpContext();
            httpContextThreadLocal.set(httpContext);
        }
        return httpContext;
    }

    public static final void setContext(HttpContext httpContext) {
        Assert.notNull(httpContext, "Only non-null context instances are permitted");
        httpContextThreadLocal.set(httpContext);
    }

    private static final HttpContext createHttpContext() {
        return new HttpContext();
    }
}
