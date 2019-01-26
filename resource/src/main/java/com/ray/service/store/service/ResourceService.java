package com.ray.service.store.service;

import com.ray.service.store.mapper.ResourceMapper;
import com.ray.service.store.model.Account;
import com.ray.service.store.model.Log;
import com.ray.service.store.model.LogDetial;
import com.ray.service.store.model.Reply;
import com.ray.service.store.util.NetFile;
import com.ray.service.store.util.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ResourceService {
    @Value("${rource.upload.dir.root}")
    private String saveDirRoot;
    @Autowired
    private ResourceMapper resourceMapper;

    public Log createLog(Account account, String title,
                         int gid, int type) {
        Log logFile = new Log();
        logFile.setTitle(title);
        logFile.setUid(account.getUid());
        logFile.setGid(gid);
        logFile.setType(type);
        logFile.setDate(SimpleDate.getUTCTime());
        resourceMapper.createLog(logFile);
        LogDetial logDetial = new LogDetial();
        logDetial.setLid(logFile.getId());
        logDetial.setRes_url(NetFile.getDirAbsolutePath(saveDirRoot,
                String.valueOf(account.getUid()),
                SimpleDate.getUTCTimeStr(),
                String.valueOf(logFile.getId())));
        resourceMapper.createLogDetial(logDetial);
        logFile.setDetial(logDetial);
        return logFile;
    }

    public int removeLog(Log log){
        resourceMapper.changeLogStatus(log.getId(), Log.STATUS_LOCKED);
        for(Reply reply:log.getReplyList()){
            resourceMapper.deleteReply(reply.getId());
        }
        log.getReplyList().clear();
        resourceMapper.deleteLogDetial(log.getDetial().getId());
        int ret = resourceMapper.deleteLog(log.getId());
        log.reset();
        return ret;
    }

    public Log changeLog(Log log){
        LogFile logFile = resourceMapper.queryLogById(logid);
        if(logFile == null){
            throw new IOException("Invalid ID");
        }
        logFile.getDetial().addMultMediaUrl(file);
        resourceMapper.updateLogDetial(logFile.getDetial());
        return logFile;
    }

    public ReplyLog createReply(Account account, long logid, String msg) {
        ReplyLog replyLog = new ReplyLog();
        replyLog.setLogid(logid);
        replyLog.setMsg(msg);
        replyLog.setUid(account.getUid());
        replyLog.setTime(SimpleDate.getCurrentTime());
        resourceMapper.createReply(replyLog);
        return replyLog;
    }

    public int updateLog(LogFile log) {
        int ret = 0;
        if (resourceMapper.updateLogDetial(log.getDetial()) > 0) {
            ret = resourceMapper.updateLog(log);
        }
        return ret;
    }

    public int deleteLog(LogFile log) {
        resourceMapper.deleteLogDetial(log.getDetial().getId());
        for(ReplyLog rl:log.getReplyLogs()){
            resourceMapper.deleteReply(rl.getId());
        }
        return resourceMapper.deleteLog(log.getId());
    }

    public LogFile deleteReply(LogFile log, long uid, long rid) {
        List<ReplyLog> replyLogList = log.getReplyLogs();
        for(ReplyLog rl : replyLogList){
            if(rl.getId() == rid && rl.getUid() == uid){
                resourceMapper.deleteReply(rid);
                log.getReplyLogs().remove(rl);
                break;
            }
        }
        return log;
    }

    public List<ReplyLog> queryLogReplyByLogId(long id){
        return resourceMapper.queryLogReplyByLogId(id);
    }


    public List<LogFile> getAllCreatedLogFileBy(long uid, long logid){
        return resourceMapper.queryAllLogFile(uid, logid);
    }
}
