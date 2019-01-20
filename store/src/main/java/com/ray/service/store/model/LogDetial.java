package com.ray.service.store.model;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogDetial implements Serializable {
    private long id;
    private String shortDesc;
    private String multMediaDirRoot;
    private String multMediaFiles;
    private List<String> multMediaUrls;
    private String extendUrl;

    public void addMultMediaUrl(String url) {
        getMultMediaUrls();
        if (multMediaUrls == null) {
            multMediaUrls = new ArrayList<>();
        }
        multMediaUrls.add(url);
        multMediaFiles = null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getMultMediaDirRoot() {
        return multMediaDirRoot;
    }

    public void setMultMediaDirRoot(String multMediaDirRoot) {
        this.multMediaDirRoot = multMediaDirRoot;
    }

    public String getMultMediaFiles() {
        if (multMediaFiles == null && multMediaUrls != null) {
            multMediaFiles = StringUtils.join(multMediaUrls, ";");
        }
        return multMediaFiles;
    }

    public void setMultMediaFiles(String multMediaFiles) {
        this.multMediaFiles = multMediaFiles;
    }

    public List<String> getMultMediaUrls() {
        if (multMediaUrls == null && multMediaFiles != null) {
            multMediaUrls = new ArrayList<>();
            for (String filename : multMediaFiles.split(";")) {
                multMediaUrls.add(filename);
            }
        }
        return multMediaUrls;
    }

    public void setMultMediaUrls(List<String> multMediaUrls) {
        this.multMediaUrls = multMediaUrls;
    }

    public String getExtendUrl() {
        return extendUrl;
    }

    public void setExtendUrl(String extendUrl) {
        this.extendUrl = extendUrl;
    }
}
