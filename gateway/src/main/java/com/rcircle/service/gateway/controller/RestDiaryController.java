package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.model.Category;
import com.rcircle.service.gateway.model.LogFile;
import com.rcircle.service.gateway.services.ResourceService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/diary")
public class RestDiaryController {
    @Resource
    private ResourceService resourceService;

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
        Category category = resourceService.addCategory(type);
        LogFile logFile = resourceService.createNewLog(title, category.getCid(), 0);
        return String.valueOf(logFile.getId());
    }


    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam(name = "filename") String filename) {
        return "";
    }
}
