package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.model.LogFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/debug")
public class DebugController {
    @GetMapping("join")
    public String signup(ModelMap mm){
        mm.addAttribute("title", "Join RC");
        return "sign_up";
    }

    @GetMapping("login")
    public String login(ModelMap mm){
        mm.addAttribute("title", "Login");
        return "login";
    }

    @GetMapping("log")
    public String createLog(ModelMap mm){
        mm.addAttribute("title","Create new log");
        mm.addAttribute("logfile", new LogFile());
        return "log_edit";
    }
}
