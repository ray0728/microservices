package com.rcircle.service.resource.service;

import com.rcircle.service.resource.mapper.ResourceMapper;
import com.rcircle.service.resource.model.*;
import com.rcircle.service.resource.utils.NetFile;
import com.rcircle.service.resource.utils.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ResourceService {
    @Value("${resource.upload.dir.root}")
    private String saveDirRoot;
    @Autowired
    private ResourceMapper resourceMapper;

    public Log createLog(Log log) {
        Category category = createNewCategory(log.getUid(), log.getCategory().getDesc());
        log.getCategory().setCid(category.getCid());
        log.getCategory().setId(category.getId());
        log.getCategory().setUid(category.getUid());
        resourceMapper.createLog(log);
        createLogDetail(log, "");
        return log;
    }

    private Log createLogDetail(Log log, String content) {
        LogDetail logDetail = new LogDetail();
        logDetail.setLid(log.getId());
        logDetail.setRes_url(NetFile.getDirAbsolutePath(saveDirRoot,
                String.valueOf(log.getUid()),
                SimpleDate.today(),
                String.valueOf(log.getId())));
        if (content != null) {
            logDetail.setLog(content);
        }
        resourceMapper.createLogDetail(logDetail);
        log.setDetail(logDetail);
        return log;
    }

    public int deleteLog(Log log) {
        resourceMapper.changeLogStatus(log.getId(), Log.STATUS_LOCKED);
        List<Reply> replies = resourceMapper.getLogReplies(log.getId());
        if (replies != null) {
            for (Reply reply : replies) {
                resourceMapper.deleteReply(reply.getId());
            }
        }
        NetFile.deleteDir(new File(log.getDetail().getRes_url()));
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


    public int changeLog(Log log, String[] tags) {
        List<Tag> shouldAdd = new ArrayList<>();
        List<Tag> shouldRm = new ArrayList<>();
        if (log.checkTags(tags, shouldAdd, shouldRm)) {
            Tag tag;
            Iterator<Tag> iter = shouldAdd.iterator();
            while (iter.hasNext()) {
                tag = iter.next();
                createTag(tag);
                resourceMapper.addTagForLog(log.getId(), tag);
            }
            iter = shouldRm.iterator();
            while (iter.hasNext()) {
                tag = iter.next();
                resourceMapper.deleteTagFromLog(tag.getMid(), tag.getId(), log.getId());
            }
        }
        Category category = createNewCategory(log.getUid(), log.getCategory().getDesc());
        log.getCategory().setCid(category.getCid());
        log.getCategory().setId(category.getId());
        return resourceMapper.changeLog(log);
    }

    public int changeLogDetail(Log log) {
        return resourceMapper.changeLogDetail(log.getDetail().getId(), log.getDetail().getLog(), log.getDetail().getRes_url());
    }

    public Reply createReply(Log log, Reply reply) {
        switch (log.getStatus()) {
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

    public Reply deleteReply(Log log, Reply reply) {
        switch (log.getStatus()) {
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
        if (log != null) {
            List replies = resourceMapper.getLogReplies(log.getId());
            log.setReplies_count(replies == null ? 0 : replies.size());
            copyResFileToLog(log, "img");
            copyResFileToLog(log, "video");
            copyResFileToLog(log, "cover");
        }
        return log;
    }

    private void copyResFileToLog(Log log, String subdir) {
        if (log.getDetail() == null) {
            return;
        }
        Map<String, String> files = NetFile.getFilesInfo(log.getDetail().getRes_url(), subdir);
        if (files == null) {
            return;
        }
        for (Map.Entry<String, String> entry : files.entrySet()) {
            log.getDetail().addResFile(entry.getKey(), entry.getValue());
        }
    }


    public List<Log> getLogs(int uid, int type, int gid, int tid, String title, int status, int offset, int count, Map resultData) {
        List<Log> logs = resourceMapper.getLogs(uid, type, tid, gid, title, status, offset, count);
        resultData.putIfAbsent("count", logs.size() == 0 ? 0 : logs.get(0).getCount());
        resultData.putIfAbsent("logs", assembleResFilesInfo(logs));
        return logs;
    }

    public List<Log> getTopLogs() {
        List<Log> logs = resourceMapper.getTopLogs();
        return assembleResFilesInfo(logs);
    }

    private List<Log> assembleResFilesInfo(List<Log> logs) {
        Iterator<Log> iter = logs.iterator();
        while (iter.hasNext()) {
            Log log = iter.next();
            List replies = resourceMapper.getLogReplies(log.getId());
            log.setReplies_count(replies == null ? 0 : replies.size());
            copyResFileToLog(log, "img");
            copyResFileToLog(log, "video");
            copyResFileToLog(log, "cover");
        }
        return logs;
    }

    public List<Reply> getReplies(int lid) {
        return resourceMapper.getLogReplies(lid);
    }

    public Reply getReplyById(int id) {
        return resourceMapper.getReply(id);
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
