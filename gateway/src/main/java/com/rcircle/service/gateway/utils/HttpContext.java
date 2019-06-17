package com.rcircle.service.gateway.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class HttpContext {

    private Map<String, Object> contextMap = new HashMap<>();

    public void setValue(String key, Object value) {
        contextMap.put(key, value);
    }

    public Object getValue(String key) {
        return contextMap.get(key);
    }

    public String[] getStringArrayValue(String key){
        return new String[]{contextMap.get(key).toString()};
    }

    public void delete(String key){
        if(contextMap.containsKey(key)){
            contextMap.remove(key);
        }
    }

    public boolean isEmpty(){
        return contextMap.isEmpty();
    }

    public Map<String, Object> getMap(){
        return contextMap;
    }

    public void clear(){
        contextMap.clear();
    }
}
