package com.rcircle.service.gateway.model;

import java.io.Serializable;

public class Quotation implements Serializable {
    private int id;
    private String desc_chinese;
    private String desc_english;
    private String author;
    private String source;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc_chinese() {
        return desc_chinese;
    }

    public void setDesc_chinese(String desc_chinese) {
        this.desc_chinese = desc_chinese;
    }

    public String getDesc_english() {
        return desc_english;
    }

    public void setDesc_english(String desc_english) {
        this.desc_english = desc_english;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDesc() {
        return desc_english == null ? desc_chinese : desc_english;
    }
}
