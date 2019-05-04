package com.rcircle.service.gateway.model;

import com.rcircle.service.gateway.utils.Toolkit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LogDetail implements Serializable {
    private int id;
    private int lid;
    private String log;
    private String res_url;
    private Map<String, String> files;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public String getRes_url() {
        return res_url;
    }

    public void setRes_url(String res_url) {
        this.res_url = res_url;
    }

    public String getLog() {
        return log;
    }

    public String getDesc(int maxnum) {
        String htmllog = "";
        if (log != null) {
            htmllog = Toolkit.getTextFromHtml(log);
            htmllog = htmllog.substring(0, maxnum > htmllog.length() ? htmllog.length() : maxnum);
        }
        return htmllog;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Map<String, String> getFiles() {
        if (files == null) {
            files = new HashMap<>();
        }
        return files;
    }

}
