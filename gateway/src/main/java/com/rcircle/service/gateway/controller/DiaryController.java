package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.model.Category;
import com.rcircle.service.gateway.model.LogFile;
import com.rcircle.service.gateway.services.ResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/diary")
public class DiaryController {
    @Resource
    private ResourceService resourceService;

    @GetMapping("/new")
    public String createNewDiary(ModelMap mm){
        List<Category> categories= resourceService.getAllCategoryForCurrentUser();
        mm.addAttribute("title","Create new log");
        mm.addAttribute("logfile", new LogFile());
        mm.addAttribute("categories", categories);
        mm.addAttribute("lid", 0);
        return "newlog";
    }

    @PostMapping("append")
    public String appendLog(@RequestParam(name = "resid", required = true) int id,
                            @RequestParam(name = "log", required = true) String log) {
        return "redirect:/diary/list";
    }
}
