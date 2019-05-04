package com.rcircle.service.resource.service;

import com.rcircle.service.resource.mapper.ResourceMapper;
import com.rcircle.service.resource.model.*;
import com.rcircle.service.resource.utils.NetFile;
import com.rcircle.service.resource.utils.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ResourceService {
    @Value("${rource.upload.dir.root}")
    private String saveDirRoot;
    @Autowired
    private ResourceMapper resourceMapper;

    public Log createLog(Log log) {
        Category category = createNewCategory(log.getUid(), log.getCategory().getDesc());
        log.getCategory().setCid(category.getCid());
        log.getCategory().setId(category.getId());
        log.getCategory().setUid(category.getUid());
        resourceMapper.createLog(log);
        Iterator<Tag> iter = log.getTags().iterator();
        while (iter.hasNext()) {
            Tag tag = iter.next();
            createTag(tag);
            resourceMapper.addTagForLog(log.getId(), tag);
        }
        return log;
    }

    public Log createLogDetail(Log log){
        LogDetail logDetail = new LogDetail();
        logDetail.setLid(log.getId());
        logDetail.setRes_url(NetFile.getDirAbsolutePath(saveDirRoot,
                String.valueOf(log.getUid()),
                SimpleDate.getUTCTimeStr(),
                String.valueOf(log.getId())));
        resourceMapper.createLogDetail(logDetail);
        log.setDetail(logDetail);
        return log;
    }

    public int deleteLog(Log log) {
        resourceMapper.changeLogStatus(log.getId(), Log.STATUS_LOCKED);
        for (Reply reply : log.getReplyList()) {
            resourceMapper.deleteReply(reply.getId());
        }
        log.getReplyList().clear();
        resourceMapper.deleteLogDetial(log.getDetail().getId());
        int ret = resourceMapper.deleteLog(log.getId());
        Iterator<Tag> iter = log.getTags().iterator();
        while (iter.hasNext()) {
            Tag tag = iter.next();
            resourceMapper.deleteTagFromLog(tag.getMid(), tag.getId(), log.getId());
        }
        log.reset();
        return ret;
    }


    public int changeLog(Log log) {
        Category category = createNewCategory(log.getUid(), log.getCategory().getDesc());
        log.getCategory().setCid(category.getCid());
        log.getCategory().setId(category.getId());
        return resourceMapper.changeLog(log);
    }

    public int changeLogDetail(Log log) {
        return resourceMapper.changeLogDetail(log.getId(), log.getDetail().getLog(), log.getDetail().getRes_url());
    }

    public Reply createReply(Reply reply) {
        int status = resourceMapper.getLogStatus(reply.getLid());
        switch (status) {
            case Log.STATUS_NORMAL:
                resourceMapper.createReply(reply);
                break;
            case Log.STATUS_DISABLE:
            case Log.STATUS_LOCKED:
                reply.setId(0);
                break;
        }
        return reply;
    }

    public Reply deleteReply(Reply reply) {
        int status = resourceMapper.getLogStatus(reply.getLid());
        switch (status) {
            case Log.STATUS_NORMAL:
            case Log.STATUS_DISABLE:
                resourceMapper.deleteReply(reply.getId());
                break;
            case Log.STATUS_LOCKED:
                reply.setId(0);
                break;
        }
        return reply;
    }

    public Log getLog(int id) {
        Log log = resourceMapper.getLogById(id);
        copyResFileToLog(log, "img");
        copyResFileToLog(log, "video");
        return log;
    }

    private void copyResFileToLog(Log log, String subdir){
        if (log.getDetail() == null) {
            return;
        }
        Map<String, String> files = NetFile.getFilesInfo(log.getDetail().getRes_url(), subdir);
        if(files == null){
            return;
        }
        for (Map.Entry<String, String> entry : files.entrySet()) {
            log.getDetail().addResFile(entry.getKey(), entry.getValue());
        }
    }


    public List<Log> getLogs(int uid, int type, int gid, String title, int status, int offset, int count) {
        List<Log> logs = resourceMapper.getLogs(uid, type, gid, title, status, offset, count);
        Iterator<Log> iter = logs.iterator();
        while (iter.hasNext()) {
            Log log = iter.next();
            copyResFileToLog(log, "img");
            copyResFileToLog(log, "video");
        }
        return logs;
    }

    public List<Reply> getReplies(int lid) {
        return resourceMapper.getLogReplies(lid);
    }

    public List<Category> getAllCategory(int uid) {
        if (uid == 0) {
            return resourceMapper.getAllPublicCategory();
        }
        return resourceMapper.getAllCategory(uid);
    }

    public Category createNewCategory(int uid, String desc) {
        Map<String, Object> params = new HashMap<>();
        params.putIfAbsent("id", 0);
        params.putIfAbsent("desc", desc);
        Category newcategory = resourceMapper.getCategory(params);
        if (newcategory == null) {
            newcategory = new Category();
            newcategory.setDesc(desc);
            resourceMapper.createCategory(newcategory);
        }
        newcategory.setUid(uid);
        if (resourceMapper.getAccountCategoryMapId(newcategory) == null) {
            resourceMapper.addUserDefCategory(newcategory);
        }
        return newcategory;
    }

    public int deleteUserDefCategory(int uid, int id) {
        return resourceMapper.deleteUserDefCategory(id, uid);
    }

    public int deleteCategory(int id, String desc) {
        return resourceMapper.deleteCategory(id, desc);
    }

    public List<Tag> getAllTags(int uid) {
        return resourceMapper.getAllTags(uid);
    }

    public Tag createTag(Tag tag) {
        Tag tmp = resourceMapper.getTag(tag.getDesc());
        if (tmp == null) {
            resourceMapper.createTag(tag);
        } else {
            tag.setId(tmp.getId());
        }
        return tag;
    }

}
