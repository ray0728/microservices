package com.ray.service.store.service;

import com.ray.service.store.mapper.ResourceMapper;
import com.ray.service.store.model.Account;
import com.ray.service.store.model.LogDetial;
import com.ray.service.store.model.LogFile;
import com.ray.service.store.model.ReplyLog;
import com.ray.service.store.util.NetFile;
import com.ray.service.store.util.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ResourceService {
    @Value("${rource.upload.dir.root}")
    private String saveDirRoot;
    @Autowired
    private ResourceMapper resourceMapper;

    public LogFile createLog(Account account, String title,
                             String description, String extendUrl) {
        LogFile logFile = new LogFile();
        logFile.setTitle(title);
        logFile.setCreatorid(account.getUid());
        logFile.setCreatetime(SimpleDate.getCurrentTime());
        resourceMapper.createLog(logFile);
        LogDetial logDetial = new LogDetial();
        logDetial.setMultMediaDirRoot(NetFile.getDirAbsolutePath(saveDirRoot,
                String.valueOf(account.getUid()),
                SimpleDate.getCurrentDate(),
                String.valueOf(logFile.getId())));
        if (description != null) {
            logDetial.setShortDesc(description);
        }
        if (extendUrl != null) {
            logDetial.setExtendUrl(extendUrl);
        }
        resourceMapper.createLogDetial(logDetial);
        logFile.setLogid(logDetial.getId());
        logFile.setDetial(logDetial);
        resourceMapper.updateLog(logFile);
        return logFile;
    }

    public LogFile updateResourceFiles(Account account, long logid, String file) throws IOException {
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
