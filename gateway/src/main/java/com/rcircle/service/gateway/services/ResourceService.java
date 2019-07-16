package com.rcircle.service.gateway.services;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.gateway.clients.RemoteResourceClient;
import com.rcircle.service.gateway.model.*;
import com.rcircle.service.gateway.utils.HttpContextHolder;
import com.rcircle.service.gateway.utils.Toolkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceService {
    @Autowired
    private RemoteResourceClient remoteResourceClient;

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllCategory", threadPoolKey = "CategoryThreadPool")
    public List<Category> getAllCategoryForCurrentUser() {
        String ret = remoteResourceClient.getAllCategorys();
        Map<String, Object> map = new HashMap<>();
        map.put("list_category", Category.class);
        if (Toolkit.parseResultData(ret, map)) {
            return (List<Category>) map.get("list_category");
        }
        return buildFallbackGetAllCategory(null);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackAddCategory", threadPoolKey = "CategoryThreadPool")
    public Category addCategory(String desc) {
        String ret = remoteResourceClient.addNewCategory(desc);
        Map<String, Object> map = new HashMap<>();
        map.put("category", Category.class);
        if (Toolkit.parseResultData(ret, map)) {
            return (Category) map.get("category");
        }
        return buildFallbackAddCategory(desc, null);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllDiaries", threadPoolKey = "DirayThreadPool")
    public int getAllBlogs(int type, int gid, String title, int status, int offset, int count, List<LogFile> logs) {
        String ret = remoteResourceClient.getAllDiaries(type, gid, title, status, offset, count);
        Map<String, Object> map = new HashMap<>();
        map.put("list_log", LogFile.class);
        map.put("count", Integer.class);
        if (Toolkit.parseResultData(ret, map)) {
            Toolkit.copy((List) map.get("list_log"), logs);
            return (int) map.get("count");
        }
        return buildFallbackGetAllDiaries(type, gid, title, status, offset, count, logs, null);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllTags", threadPoolKey = "DirayThreadPool")
    public List<Tag> getAllTags() {
        String ret = remoteResourceClient.getAllTags();
        Map<String, Object> map = new HashMap<>();
        map.put("list_tag", Tag.class);
        if (Toolkit.parseResultData(ret, map)) {
            return (List<Tag>) map.get("list_tag");
        }
        return buildFallbackGetAllTags(null);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetTopDiaries", threadPoolKey = "DirayThreadPool")
    public List<LogFile> getTopBlogs() {
        List<LogFile> logs = null;
        String ret = remoteResourceClient.getTopResource();
        Map<String, Object> map = new HashMap<>();
        map.put("list_log", LogFile.class);
        if (Toolkit.parseResultData(ret, map)) {
            logs = (List<LogFile>) map.get("list_log");
        }
        return logs == null ? buildFallbackGetTopDiaries(null) : logs;
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetDiary", threadPoolKey = "DirayThreadPool")
    public LogFile getBlog(int id, boolean force) {
        if (force) {
            HttpContextHolder.getContext().setValue("rc-resource-security", Toolkit.randomString(8));
        }
        String ret = remoteResourceClient.getBLog(id);
        Map<String, Object> map = new HashMap<>();
        map.put("log", LogFile.class);
        if (Toolkit.parseResultData(ret, map)) {
            return (LogFile) map.get("log");
        }
        if (force) {
            HttpContextHolder.getContext().delete("rc-resource-security");
        }
        return buildFallbackGetDiary(id, force, null);
    }

    @HystrixCommand(fallbackMethod = "buildFallbackGetAllReplies", threadPoolKey = "ReplyThreadPool")
    public List<Reply> getAllReplies(int logid) {
        String ret = remoteResourceClient.getAllReplies(logid);
        Map<String, Object> map = new HashMap<>();
        map.put("list_reply", Reply.class);
        if (Toolkit.parseResultData(ret, map)) {
            return (List<Reply>) map.get("list_reply");
        }
        return buildFallbackGetAllReplies(logid, null);
    }

    public List<Reply> buildFallbackGetAllReplies(int logid, Throwable throwable) {
        return null;
    }

    public List<Category> buildFallbackGetAllCategory(Throwable throwable) {
        return new ArrayList<>();
    }

    public Category buildFallbackAddCategory(String desc, Throwable throwable) {
        return null;
    }

    public int buildFallbackGetAllDiaries(int type, int gid, String title, int status, int offset, int count, List<LogFile> logs, Throwable throwable) {
        return 0;
    }

    public List<Tag> buildFallbackGetAllTags(Throwable throwable) {
        return new ArrayList<>();
    }

    public List<LogFile> buildFallbackGetTopDiaries(Throwable throwable) {
        return new ArrayList<>();
    }

    public LogFile buildFallbackGetDiary(int id, boolean force, Throwable throwable) {
        return null;
    }
}
