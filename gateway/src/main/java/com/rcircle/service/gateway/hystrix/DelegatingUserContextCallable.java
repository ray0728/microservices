package com.rcircle.service.gateway.hystrix;


import com.rcircle.service.gateway.utils.HttpContext;
import com.rcircle.service.gateway.utils.HttpContextHolder;

import java.util.concurrent.Callable;

public final class DelegatingUserContextCallable<V> implements Callable<V> {
    private final Callable<V> delegate;
    private HttpContext httpContext;

    public DelegatingUserContextCallable(Callable<V> delegate, HttpContext context) {
        this.delegate = delegate;
        this.httpContext = context;
    }

    @Override
    public V call() throws Exception {
        HttpContextHolder.setContext(httpContext);
        try {
            return delegate.call();
        }finally {
            httpContext = null;
        }
    }

    public static <V> Callable<V> create(Callable<V> delegate, HttpContext httpContext){
        return new DelegatingUserContextCallable<V>(delegate, httpContext);
    }
}
