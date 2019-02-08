package com.rcircle.service.resource.utils;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class HttpContextInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HttpContext.AUTH_TOKEN, HttpContextHolder.getContext().getValue(HttpContext.AUTH_TOKEN));
    }
}
