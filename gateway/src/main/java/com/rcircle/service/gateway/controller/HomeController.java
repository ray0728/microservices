package com.rcircle.service.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("securedPage")
    public String showSecuredPage(){return  "securePage";}

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
}
