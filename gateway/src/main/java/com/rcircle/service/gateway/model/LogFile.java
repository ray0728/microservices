package com.rcircle.service.gateway.model;

import java.io.Serializable;

public class LogFile implements Serializable {
    private int id;
    private String title;
    private Category category;
    private String htmllog;

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getHtmllog() {
        return htmllog;
    }

    public void setHtmllog(String htmllog) {
        this.htmllog = htmllog;
    }
}
