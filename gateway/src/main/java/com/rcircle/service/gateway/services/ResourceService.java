package com.rcircle.service.gateway.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.gateway.clients.RemoteResourceClient;
import com.rcircle.service.gateway.model.*;
import com.rcircle.service.gateway.utils.Toolkit;
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
        ResultData data = JSON.parseObject(ret, ResultData.class);
        return JSON.parseArray(data.getMap().get("categories").toString(), Category.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackAddCategory", threadPoolKey = "CategoryThreadPool")
    public Category addCategory(String desc) {
        String ret = remoteResourceClient.addNewCategory(desc);
        ResultData data = JSON.parseObject(ret, ResultData.class);
        return JSON.parseObject(data.getMap().get("category").toString(), Category.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllDiaries", threadPoolKey = "DirayThreadPool")
    public int getAllBlogs(int type, int gid, String title, int status, int offset, int count, List<LogFile> logs) {
        String ret = remoteResourceClient.getAllDiaries(type, gid, title, status, offset, count);
        ResultData data = JSON.parseObject(ret, ResultData.class);
        Toolkit.copy(JSON.parseArray(data.getMap().get("logs").toString(), LogFile.class),logs);
        return (int) data.getMap().get("count");
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllTags", threadPoolKey = "DirayThreadPool")
    public List<Tag> getAllTags() {
        String ret = remoteResourceClient.getAllTags();
        ResultData data = JSON.parseObject(ret, ResultData.class);
        return JSON.parseArray(data.getMap().get("tags").toString(), Tag.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetTopDiaries", threadPoolKey = "DirayThreadPool")
    public List<LogFile> getTopBlogs() {
        String ret = remoteResourceClient.getTopResource();
        ResultData data = JSON.parseObject(ret, ResultData.class);
        return JSON.parseArray(data.getMap().get("logs").toString(), LogFile.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetDiary", threadPoolKey = "DirayThreadPool")
    public LogFile getBlog(int id) {
        String ret = remoteResourceClient.getBLog(id);
        ResultData data = JSON.parseObject(ret, ResultData.class);
        return JSON.parseObject(data.getMap().get("log").toString(), LogFile.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllReplies", threadPoolKey = "ReplyThreadPool")
    public List<Reply> getAllReplies(int logid) {
        String ret = remoteResourceClient.getAllReplies(logid);
        ResultData data = JSON.parseObject(ret, ResultData.class);
        return JSON.parseArray(data.getMap().get("replies").toString(), Reply.class);
    }

    public List<Reply> buildFallbackGetAllReplies(int logid, Throwable throwable) {
        return null;
    }

    public List<Category> buildFallbackGetAllCategory(Throwable throwable) {
        return null;
    }

    public Category buildFallbackAddCategory(String desc, Throwable throwable) {
        return null;
    }

    public int buildFallbackGetAllDiaries(int type, int gid, String title, int status, int offset, int count, List<LogFile> logs, Throwable throwable) {
        return 0;
    }

    public List<Tag> buildFallbackGetAllTags(Throwable throwable) {
        return null;
    }

    public List<LogFile> buildFallbackGetTopDiaries(Throwable throwable) {
        return null;
    }

    public LogFile buildFallbackGetDiary(int id, Throwable throwable) {
        return null;
    }
}
