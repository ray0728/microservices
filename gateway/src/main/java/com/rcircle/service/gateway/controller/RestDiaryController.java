package com.rcircle.service.gateway.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/diary")
public class RestDiaryController {
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
