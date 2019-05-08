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

    @GetMapping("/edit")
    public String createNewDiary(ModelMap mm, @RequestParam(name = "id", required = false, defaultValue = "0") int id) {
        MvcToolkit.autoLoadTopMenuData(resourceService, mm);
        LogFile log = null;
        if (id != 0) {
            log = resourceService.getBlog(id);
        }
        if (log == null) {
            log = new LogFile();
            id = 0;
        }
        mm.addAttribute("title", id == 0 ? "Create New Blog" : "Edit - " + log.getTitle());
        mm.addAttribute("logfile", log);
        mm.addAttribute("res_id", id);
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
        if (log == null) {
            return "redirect:/error?type=404";
        } else {
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
            mm.addAttribute("replies", resourceService.getAllReplies(log.getId()));
        }
        return "blog_show";
    }

    @GetMapping("reply")
    public String showAllReplies(@RequestParam(name="id")int id, ModelMap mm){
        mm.addAttribute("replies", resourceService.getAllReplies(id));
        return "blog_show::reply-list";
    }
}
