package com.ray.service.store.model;

import java.io.Serializable;
import java.util.List;

public class Log implements Serializable {
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_DISABLE = 1;
    public static final int STATUS_LOCKED = 2;
    private int id;
    private String title;
    private int uid;
    private long date;
    private int gid;
    private int type;
    private int like_num;
    private int unlike_num;
    private int status;

    private LogDetial detial;

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

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public int getUnlike_num() {
        return unlike_num;
    }

    public void setUnlike_num(int unlike_num) {
        this.unlike_num = unlike_num;
    }

    public LogDetial getDetial() {
        return detial;
    }

    public void setDetial(LogDetial detial) {
        this.detial = detial;
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

    public void reset(){
        id = 0;
        title = null;
        replyList.clear();
    }
}
