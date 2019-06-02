package com.rcircle.service.gateway.model;

import java.util.HashMap;

public class HLSMap extends HashMap{

    public int getId() {
        return (int)get("id");
    }
    public String getName() {
        return (String)get("name");
    }
    public boolean isSuccess() {
        return (boolean)get("result");
    }

    public boolean isPointSameFile(int logid, String filename){
        return getId() == logid && getName().equals(filename);
    }
}
