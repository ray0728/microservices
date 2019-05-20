package com.rcircle.service.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.gateway.utils.Toolkit;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rst")
public class RestPageController {
    private static final int AI_TYPE_IP = 0;
    private static final int AI_TYPE_PORT = 1;
    private static final int AI_TYPE_IP_AND_PORT = 2;

    @GetMapping("/redirect")
    public String authcode(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state) {
        Map<String, String> result = new HashMap<>();
        result.put("code", code);
        result.put("state", state);
        return JSONObject.toJSONString(result);
    }

    @GetMapping("/ai")
    public String ai(@RequestParam(name="type")int type){
        String port = "10006";
        String ip = Toolkit.getLocalIP();
        switch (type){
            case AI_TYPE_IP:
                return ip;
            case AI_TYPE_PORT:
                return port;
            case AI_TYPE_IP_AND_PORT:
                return ip + ":" +port;
        }
        return "";
    }
}
