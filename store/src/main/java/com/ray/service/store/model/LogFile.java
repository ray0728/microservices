package com.ray.service.store.model;

import java.io.Serializable;
import java.util.List;

public class LogFile implements Serializable {
    private long id = 0;
    private long logid = 0;
    private String title;
    private long creatorid;
    private long createtime;
    private long modifyid;
    private long modifytime;
    private LogDetial detial;
    private List<ReplyLog> replyLogs;
    private String errinfo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLogid() {
        return logid;
    }

    public void setLogid(long logid) {
        this.logid = logid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(long creatorid) {
        this.creatorid = creatorid;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getModifyid() {
        return modifyid;
    }

    public void setModifyid(long modifyid) {
        this.modifyid = modifyid;
    }

    public long getModifytime() {
        return modifytime;
    }

    public void setModifytime(long modifytime) {
        this.modifytime = modifytime;
    }

    public LogDetial getDetial() {
        return detial;
    }

    public void setDetial(LogDetial detial) {
        this.detial = detial;
    }

    public List<ReplyLog> getReplyLogs() {
        return replyLogs;
    }

    public void setReplyLogs(List<ReplyLog> replyLogs) {
        this.replyLogs = replyLogs;
    }

    public String getErrinfo() {
        return errinfo;
    }

    public void setErrinfo(String errinfo) {
        this.errinfo = errinfo;
    }

    public boolean hasError(){
        return errinfo != null && !errinfo.isEmpty();
    }
}
