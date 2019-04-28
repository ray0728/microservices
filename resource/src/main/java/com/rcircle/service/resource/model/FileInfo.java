package com.rcircle.service.resource.model;

import java.io.Serializable;

public class FileInfo implements Serializable {
    private String name;
//    private long size;
//    private String checksum;
    private String errinfo;
    private String mime;
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public long getSize() {
//        return size;
//    }
//
//    public void setSize(long size) {
//        this.size = size;
//    }
//
//    public String getChecksum() {
//        return checksum;
//    }
//
//    public void setChecksum(String checksum) {
//        this.checksum = checksum;
//    }

    public String getErrinfo() {
        return errinfo;
    }

    public void setErrinfo(String errinfo) {
        this.errinfo = errinfo;
    }

    public String getMime() {
        return mime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }
}