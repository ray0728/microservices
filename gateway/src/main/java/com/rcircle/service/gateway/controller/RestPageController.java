package com.rcircle.service.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
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

    @PostMapping("/upload")
    public String uploadFiles(MultipartFile file,
                              @RequestParam(name = "resid") int id,
                              @RequestParam(name = "index") int index,
                              @RequestParam(name = "count") int count,
                              @RequestParam(name = "filename") String filename,
                              @RequestParam(name = "chunksize") int chunksize,
                              @RequestParam(name = "checksum") String checksum) {
        return "";
    }

    @PostMapping("new")
    public String createNewLog(@RequestParam(name = "title", required = true) String title,
                               @RequestParam(name = "type", required = true) String type) {
        return "1";
    }

    @PostMapping("append")
    public String appendLog(@RequestParam(name = "resid", required = true) int id,
                            @RequestParam(name = "log", required = true) String log) {
        return "";
    }


    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam(name = "filename") String filename) {
        return "";
    }
}
