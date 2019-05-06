package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.model.Author;
import com.rcircle.service.gateway.model.LogFile;
import com.rcircle.service.gateway.services.ResourceService;
import com.rcircle.service.gateway.utils.MvcToolkit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping("/blog")
public class BlogController {
    @Resource
    private ResourceService resourceService;

    @GetMapping("/new")
    public String createNewDiary(ModelMap mm) {
        MvcToolkit.autoLoadTopMenuData(resourceService, mm);
        mm.addAttribute("title", "Create New Blog");
        mm.addAttribute("logfile", new LogFile());
        mm.addAttribute("res_id", 0);
        return "blog_edit";
    }

    @GetMapping("list")
    public String showAllLogs(ModelMap mm) {
        MvcToolkit.autoLoadTopMenuData(resourceService, mm);
        MvcToolkit.autoLoadSideBarData(resourceService, mm);
        mm.addAttribute("title", "Blog List");
        mm.addAttribute("logs", resourceService.getAllBlogs(0, 0, null, 0, 0, 5));
        return "blog_list";
    }

    @GetMapping("article")
    public String showLog(@RequestParam(name = "id") int lid, ModelMap mm) {
        LogFile log = resourceService.getBlog(lid);
        if(log == null){
            return "redirect:/err?type=404";
        }else {
            MvcToolkit.autoLoadTopMenuData(resourceService, mm);
            MvcToolkit.autoLoadSideBarData(resourceService, mm);
            Author author = new Author();
            author.setId(log.getUid());
            author.setName(log.getAuthor());
            author.setDesc("Nothing");
            mm.addAttribute("title", log.getTitle());
            mm.addAttribute("log", log);
            mm.addAttribute("context", log.getDetail().getLog());
            mm.addAttribute("author", author);
        }
        return "blog_show";
    }
}
