package com.rcircle.service.gateway.controller;

import org.springframework.stereotype.Controller;
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
}
