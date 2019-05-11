package com.rcircle.service.resource.model;

import java.io.Serializable;
import java.util.ArrayList;
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
    private int status;
    private String author;
    private Category category;
    private List<Tag> tags;
    private int replies_count;

    private LogDetail detail;

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

    public LogDetail getDetail() {
        return detail;
    }

    public void setDetail(LogDetail detial) {
        this.detail = detial;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Tag> getTags() {
        if(tags == null){
            tags = new ArrayList<>();
        }
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag){
        if(tags == null){
            tags = new ArrayList<>();
        }
        tags.add(tag);
    }

    public int getReplies_count() {
        return replies_count;
    }

    public void setReplies_count(int replies_count) {
        this.replies_count = replies_count;
    }

    public void reset(){
        id = 0;
        title = null;
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

    public void setCategory(String desc){
        if(category == null){
            category = new Category();
        }
        category.setDesc(desc);
    }
}
