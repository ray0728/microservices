package com.rcircle.service.gateway.hystrix;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import com.rcircle.service.gateway.utils.HttpContextHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy {
    private HystrixConcurrencyStrategy hystrixConcurrencyStrategy;

    public ThreadLocalAwareStrategy(
            HystrixConcurrencyStrategy existingConcurrencyStrategy) {
        hystrixConcurrencyStrategy = existingConcurrencyStrategy;
    }

    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return hystrixConcurrencyStrategy == null ?
                super.getBlockingQueue(maxQueueSize) : hystrixConcurrencyStrategy.getBlockingQueue(maxQueueSize);
    }

    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
        return hystrixConcurrencyStrategy == null ?
                super.getRequestVariable(rv) : hystrixConcurrencyStrategy.getRequestVariable(rv);
    }

    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime,
                                            TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        return hystrixConcurrencyStrategy == null ?
                super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue)
                : hystrixConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixThreadPoolProperties threadPoolProperties) {
        return hystrixConcurrencyStrategy == null ?
                super.getThreadPool(threadPoolKey, threadPoolProperties)
                : hystrixConcurrencyStrategy.getThreadPool(threadPoolKey, threadPoolProperties);
    }

    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return hystrixConcurrencyStrategy == null ?
                super.wrapCallable(new DelegatingUserContextCallable<T>(callable, HttpContextHolder.getContext()))
                : hystrixConcurrencyStrategy.wrapCallable(new DelegatingUserContextCallable<T>(callable, HttpContextHolder.getContext()));
    }
}
