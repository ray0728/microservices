package com.rcircle.service.gateway.services;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.gateway.clients.RemoteResourceClient;
import com.rcircle.service.gateway.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {
    @Autowired
    private RemoteResourceClient remoteResourceClient;

    @HystrixCommand(fallbackMethod = "buildFallbackCategory", threadPoolKey = "CreateAccountThreadPool")
    public List<Category> getAllCategoryForCurrentUser() {
        String ret = remoteResourceClient.getAllCategorys();
        return JSON.parseArray(ret, Category.class);
    }

    public List<Category> buildFallbackCategory(Throwable throwable) {

        return null;
    }
}
