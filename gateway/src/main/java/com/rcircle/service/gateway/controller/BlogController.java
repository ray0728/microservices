package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.model.Category;
import com.rcircle.service.gateway.model.LogFile;
import com.rcircle.service.gateway.services.ResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {
    @Resource
    private ResourceService resourceService;

    @GetMapping("/new")
    public String createNewDiary(ModelMap mm){
        List<Category> categories= resourceService.getAllCategoryForCurrentUser();
        mm.addAttribute("title","Create New Diary");
        mm.addAttribute("logfile", new LogFile());
        mm.addAttribute("categories", categories);
        mm.addAttribute("lid", 0);
        return "log_edit";
    }

    @GetMapping("list")
    public String showAllLog(ModelMap mm) {
        List<LogFile> logFiles = resourceService.getAllDiaries(0,0,null, 0, 0, 5);
        mm.addAttribute("title", "All Diaries");
        mm.addAttribute("diaries", logFiles);
        return "log_list";
    }
}
