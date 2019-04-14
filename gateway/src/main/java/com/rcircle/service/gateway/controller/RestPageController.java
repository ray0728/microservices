package com.rcircle.service.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rst")
public class RestPageController {

    @GetMapping("/redirect")
    public String mirror(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state) {
        Map<String, String> result = new HashMap<>();
        result.put("code", code);
        result.put("state", state);
        return JSONObject.toJSONString(result);
    }
}
