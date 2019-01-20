package com.rcircle.service.account.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }


    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }


    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }


    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new NullPointerException("applicationContext is NOT Injected!");
        }
    }

    @Override
    public void destroy() throws Exception {
        SpringContextHolder.clearHolder();
    }

    private static void clearHolder() {
        applicationContext = null;
    }
}
