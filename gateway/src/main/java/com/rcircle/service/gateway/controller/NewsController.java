package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.services.MessageService;
import com.rcircle.service.gateway.utils.MvcToolkit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/news")
public class NewsController {
    @Resource
    private MessageService messageService;
    @GetMapping("")
    public String refreshNews(ModelMap mm){
        MvcToolkit.autoLoadNewsData(messageService, mm);
        return "public::news";
    }

}
