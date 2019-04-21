package com.rcircle.service.gateway.services;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.gateway.clients.RemoteResourceClient;
import com.rcircle.service.gateway.model.Category;
import com.rcircle.service.gateway.model.LogFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {
    @Autowired
    private RemoteResourceClient remoteResourceClient;

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllCategory", threadPoolKey = "CategoryThreadPool")
    public List<Category> getAllCategoryForCurrentUser() {
        String ret = remoteResourceClient.getAllCategorys();
        return JSON.parseArray(ret, Category.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackAddCategory", threadPoolKey = "CategoryThreadPool")
    public Category addCategory(String desc) {
        String ret = remoteResourceClient.addNewCategory(desc);
        return JSON.parseObject(ret, Category.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllDiaries", threadPoolKey = "DirayThreadPool")
    public List<LogFile> getAllDiaries(){
        String ret = remoteResourceClient.getAllDiaries(0,0);
        return JSON.parseArray(ret, LogFile.class);
    }

    public List<Category> buildFallbackGetAllCategory(Throwable throwable) {
        return null;
    }

    public Category buildFallbackAddCategory(String desc, Throwable throwable) {
        return null;
    }

    public List<LogFile> buildFallbackGetAllDiaries(){
        return null;
    }
}
