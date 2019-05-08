package com.rcircle.service.gateway.services;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.gateway.clients.RemoteResourceClient;
import com.rcircle.service.gateway.model.Category;
import com.rcircle.service.gateway.model.LogFile;
import com.rcircle.service.gateway.model.Reply;
import com.rcircle.service.gateway.model.Tag;
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
    public List<LogFile> getAllBlogs(int type, int gid, String title, int status, int offset, int count) {
        String ret = remoteResourceClient.getAllDiaries(type, gid, title, status, offset, count);
        return JSON.parseArray(ret, LogFile.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllTags", threadPoolKey = "DirayThreadPool")
    public List<Tag> getAllTags() {
        String ret = remoteResourceClient.getAllTags();
        return JSON.parseArray(ret, Tag.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetTopDiaries", threadPoolKey = "DirayThreadPool")
    public List<LogFile> getTopBlogs() {
        String ret = remoteResourceClient.getTopResource();
        return JSON.parseArray(ret, LogFile.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetDiary", threadPoolKey = "DirayThreadPool")
    public LogFile getBlog(int id) {
        String ret = remoteResourceClient.getBLog(id);
        return JSON.parseObject(ret, LogFile.class);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllReplies", threadPoolKey = "ReplyThreadPool")
    public List<Reply> getAllReplies(int logid){
        String ret = remoteResourceClient.getAllReplies(logid);
        return JSON.parseArray(ret, Reply.class);
    }

    public List<Reply>buildFallbackGetAllReplies(int logid, Throwable throwable){
        return null;
    }

    public List<Category> buildFallbackGetAllCategory(Throwable throwable) {
        return null;
    }

    public Category buildFallbackAddCategory(String desc, Throwable throwable) {
        return null;
    }

    public List<LogFile> buildFallbackGetAllDiaries(int type, int gid, String title, int status, int offset, int count, Throwable throwable) {
        return null;
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
