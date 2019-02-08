package com.rcircle.service.auth.utils;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class HttpContextInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HttpContext.QUERY_ACCOUNT_SECU, HttpContextHolder.getContext().getValue(HttpContext.QUERY_ACCOUNT_SECU));
    }
}
