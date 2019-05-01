package com.rcircle.service.gateway.model;

import com.rcircle.service.gateway.utils.Toolkit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogFile implements Serializable {
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_DISABLE = 1;
    public static final int STATUS_LOCKED = 2;
    private int id;
    private String title;
    private int uid;
    private long date;
    private int gid;
    private int type;
    private int status;
    private List<Tag> tags;

    private LogDetail detail;

    private List<Reply> replyList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int scope) {
        this.gid = scope;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LogDetail getDetial() {
        return detail;
    }

    public void setDetial(LogDetail detial) {
        this.detail = detial;
    }

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
    }

    public void reset() {
        id = 0;
        title = null;
        replyList.clear();
    }

    public String getMonth() {
        return Toolkit.getMonthFrom(date);
    }

    public int getYear() {
        return Toolkit.getYearFom(date);
    }

    public int getDay() {
        return Toolkit.getDayFrom(date);
    }

    public int getComments_num() {
        return replyList == null ? 0 : replyList.size();
    }

    public String getCover() {
        List<FileInfo> files = detail.getFiles();
        if(files == null){
            return null;
        }
        Iterator<FileInfo> iter = files.iterator();
        while (iter.hasNext()){
            FileInfo file = iter.next();
            if(file.isImage()){
                return String.format("/blog/api/res/img/%d/%s", id, file.getName());
            }
        }
        return "/img/blog-img/7.jpg";
    }
}
