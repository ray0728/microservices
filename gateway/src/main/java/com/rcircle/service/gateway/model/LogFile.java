package com.rcircle.service.gateway.model;

import com.rcircle.service.gateway.utils.Toolkit;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogFile implements Serializable {
    private int id;
    private String title;
    private int uid;
    private long date;
    private int gid;
    private int status;
    private String author;
    private Category category;
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

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public LogDetail getDetail() {
        return detail;
    }

    public void setDetail(LogDetail detail) {
        this.detail = detail;
    }

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
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
        if (detail != null) {
            Map<String, String> files = detail.getFiles();
            for (Map.Entry<String, String> entry : files.entrySet()) {
                if (entry.getValue().contains(File.separatorChar + "img" + File.separatorChar)) {
                    return String.format("/blog/api/res/img/%d/%s", id, entry.getKey());
                }
            }
        }
        return "global/img/blog-img/7.jpg";
    }

    public String getDesc() {
        if (detail != null) {
            return detail.getDesc(200);
        }
        return "";
    }
}
