package com.rcircle.service.resource.model;

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

    public void setLog(String log) {
        this.log = log;
    }

    public Map<String, String> getFiles() {
        if(files == null){
            files = new HashMap<>();
        }
        return files;
    }

    public void reset(){
        if(files != null) {
            files.clear();
        }
    }

    public void addResFile(String name, String path) {
        if(files == null){
            files = new HashMap<>();
        }
        files.putIfAbsent(name, path);
    }
}
