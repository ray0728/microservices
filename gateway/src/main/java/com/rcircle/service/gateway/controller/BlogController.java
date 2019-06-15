package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.model.Account;
import com.rcircle.service.gateway.model.LogFile;
import com.rcircle.service.gateway.services.AccountService;
import com.rcircle.service.gateway.services.MessageService;
import com.rcircle.service.gateway.services.ResourceService;
import com.rcircle.service.gateway.utils.MvcToolkit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {
    @Resource
    private ResourceService resourceService;
    @Resource
    private AccountService accountService;
    @Resource
    private MessageService messageService;

    @GetMapping("/edit")
    public String createNewDiary(ModelMap mm, @RequestParam(name = "id", required = false, defaultValue = "0") int id) {
        MvcToolkit.autoLoadTopMenuData(resourceService, mm);
        MvcToolkit.autoLoadNewsData(messageService, mm);
        LogFile log = null;
        if (id != 0) {
            log = resourceService.getBlog(id, false);
            mm.addAttribute("log", log);
            mm.addAttribute("context", log.getDetail().getLog());
        }
        if (log == null) {
            log = new LogFile();
            id = 0;
        }
        mm.addAttribute("title", id == 0 ? "Create New Blog" : "Edit - " + log.getTitle());
        mm.addAttribute("res_id", id);
        return "blog_edit";
    }

    @GetMapping("list")
    public String showAllLogs(ModelMap mm,
                              @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(name = "cid", required = false, defaultValue = "0") int cate,
                              @RequestParam(name = "tid", required = false, defaultValue = "0") int tag) {
        MvcToolkit.autoLoadTopMenuData(resourceService, mm);
        MvcToolkit.autoLoadSideBarData(resourceService, mm);
        MvcToolkit.autoLoadNewsData(messageService, mm);
        List<LogFile> logs = new ArrayList<>();
        String param = "";
        if (cate != 0) {
            param += "&cid=" + cate;
        }
        if (tag != 0) {
            param += "&tid=" + tag;
        }
        if(page < 0){
            page = 1;
        }
        mm.addAttribute("title", "Blog List");
        mm.addAttribute("count", resourceService.getAllBlogs(cate, 0, null, 0,
                (page - 1) * 10, 10, logs));
        mm.addAttribute("logs", logs);
        mm.addAttribute("page", page);
        mm.addAttribute("query_param", param);
        return "blog_list";
    }

    @GetMapping("page/{index}")
    public String getLogsOnPage(ModelMap mm, @PathVariable("index")int page,
                                @RequestParam(name = "cid", required = false, defaultValue = "0") int cate,
                                @RequestParam(name = "tid", required = false, defaultValue = "0") int tag) {
        List<LogFile> logs = new ArrayList<>();
        resourceService.getAllBlogs(0, 0, null, 0,
                (page - 1) * 10, 10, logs);
        mm.addAttribute("logs", logs);
        return "blog_list::log_list";
    }

    @GetMapping("article")
    public String showLog(Principal principal, @RequestParam(name = "id") int lid, ModelMap mm) {
        LogFile log = resourceService.getBlog(lid, false);
        if (log == null) {
            return "redirect:/error?type=404";
        } else {
            MvcToolkit.autoLoadTopMenuData(resourceService, mm);
            MvcToolkit.autoLoadSideBarData(resourceService, mm);
            MvcToolkit.autoLoadNewsData(messageService, mm);
            Account account = accountService.getAccountInfo(log.getUid(), null);
            mm.addAttribute("title", log.getTitle());
            mm.addAttribute("log", log);
            mm.addAttribute("context", log.getDetail().getLog());
            mm.addAttribute("author", account);
            mm.addAttribute("replies", resourceService.getAllReplies(log.getId()));
            if(principal != null) {
                Account op = accountService.getAccountInfo(0, principal.getName());
                if(op.getUid() == log.getUid()){
                    mm.addAttribute("log_id", lid);
                }
            }
            if (principal != null) {
                mm.addAttribute("reply_name", principal.getName());
                if (principal instanceof Account) {
                    mm.addAttribute("reply_email", ((Account) principal).getEmail());
                }
            }
        }
        return "blog_show";
    }

    @GetMapping("reply")
    public String showAllReplies(@RequestParam(name = "id") int id, ModelMap mm) {
        mm.addAttribute("replies", resourceService.getAllReplies(id));
        return "blog_show::reply-list";
    }
}
