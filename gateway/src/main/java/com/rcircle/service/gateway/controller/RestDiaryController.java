package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.model.Category;
import com.rcircle.service.gateway.model.LogFile;
import com.rcircle.service.gateway.services.ResourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/diary")
public class RestDiaryController {
    @Resource
    private ResourceService resourceService;

    @PostMapping("new")
    public String createNewLog(@RequestParam(name = "title", required = true) String title,
                               @RequestParam(name = "type", required = true) String type) {
        Category category = resourceService.addCategory(type);
        LogFile logFile = resourceService.createNewLog(title, category.getCid(), 0);
        return String.valueOf(logFile.getId());
    }


    @GetMapping("resurl")
    public String getFilesUrl(@RequestParam(name = "id") int id) {
        return resourceService.getResUrl(id);
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam(name = "filename") String filename) {
        return "";
    }
}
