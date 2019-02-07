package com.ray.service.resource.service;

import com.ray.service.resource.mapper.ResourceMapper;
import com.ray.service.resource.model.Log;
import com.ray.service.resource.model.LogDetail;
import com.ray.service.resource.model.Reply;
import com.ray.service.resource.utils.NetFile;
import com.ray.service.resource.utils.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {
    @Value("${rource.upload.dir.root}")
    private String saveDirRoot;
    @Autowired
    private ResourceMapper resourceMapper;

    public Log createLog(Log log) {
        resourceMapper.createLog(log);
        LogDetail logDetail = new LogDetail();
        logDetail.setLid(log.getId());
        logDetail.setRes_url(NetFile.getDirAbsolutePath(saveDirRoot,
                String.valueOf(log.getUid()),
                SimpleDate.getUTCTimeStr(),
                String.valueOf(log.getId())));
        resourceMapper.createLogDetial(logDetail);
        log.setDetial(logDetail);
        return log;
    }

    public int deleteLog(Log log) {
        resourceMapper.changeLogStatus(log.getId(), Log.STATUS_LOCKED);
        for (Reply reply : log.getReplyList()) {
            resourceMapper.deleteReply(reply.getId());
        }
        log.getReplyList().clear();
        resourceMapper.deleteLogDetial(log.getDetial().getId());
        int ret = resourceMapper.deleteLog(log.getId());
        log.reset();
        return ret;
    }

    public int changeLog(Log log) {
        return resourceMapper.changeLog(log);
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

    public Log getLogDetial(int id){
        return resourceMapper.getLogById(id);
    }

    public List<Log> getLogsByUid(int uid){
        return resourceMapper.getLogsByUid(uid);
    }

    public List<Log> getPublicLogsByType(int type){
        return resourceMapper.getPublicLogsByType(type);
    }

    public List<Log> getGroupLogsByType(int type, int gid){
        return resourceMapper.getGroupLogsByType(type, gid);
    }

    public List<Reply> getReplies(int lid){
        return resourceMapper.getLogReplies(lid);
    }
}
