package com.rcircle.service.gateway.services;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.gateway.clients.RemoteResourceClient;
import com.rcircle.service.gateway.clients.RemoteResourceUploadClient;
import com.rcircle.service.gateway.model.Category;
import com.rcircle.service.gateway.model.LogFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ResourceService {
    @Autowired
    private RemoteResourceClient remoteResourceClient;

    @Autowired
    private RemoteResourceUploadClient remoteResourceUploadClient;

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

    @HystrixCommand(fallbackMethod = "buildFallbackCreateNewLog", threadPoolKey = "ResThreadPool")
    public LogFile createNewLog(String title, int type, int gid) {
        String ret = remoteResourceClient.createNewLog(title, type, gid);
        return JSON.parseObject(ret, LogFile.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetResUrl", threadPoolKey = "ResThreadPool")
    public String getResUrl(int id) {
        return remoteResourceClient.getAllFileInfo(id, true);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackUploadFile", threadPoolKey = "ResThreadPool")
    public String uploadFile(MultipartFile file, int id, String name, int index, int total, int size, String checksum) {
        return remoteResourceUploadClient.uploadSplitFile(file, id, name, index, total, size, checksum);
    }

    public String updateLog(int id, String title, int type, int gid, String log){
        return remoteResourceClient.updateLog(id, title, type, gid, log);
    }

    public List<Category> buildFallbackGetAllCategory(Throwable throwable) {
        return null;
    }

    public Category buildFallbackAddCategory(String desc, Throwable throwable) {
        return null;
    }

    public LogFile buildFallbackCreateNewLog(String title, int type, int gid, Throwable throwable) {
        return null;
    }

    public String buildFallbackGetResUrl(int id, Throwable throwable) {
        return null;
    }

    public String buildFallbackUploadFile(int id, String name, int index,
                                          int total, int size, String checksum,
                                          Throwable throwable) {
        return null;
    }
}
