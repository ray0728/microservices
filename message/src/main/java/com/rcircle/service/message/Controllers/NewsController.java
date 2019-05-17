package com.rcircle.service.message.Controllers;

import com.rcircle.service.message.services.NewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    private NewsService newsService;

    @PostMapping("/break")
    public String sendBreakNews(Principal principal,
                                @RequestParam(name = "level") int level,
                                @RequestParam(name = "title") String title,
                                @RequestParam(name = "url") String url) {
        newsService.sendBreakNews(level, title, "root", url);
        return "";
    }

    @PostMapping("/general")
    public String sendNews(Principal principal,
                           @RequestParam(name = "title") String title,
                           @RequestParam(name = "url") String url) {
        newsService.sendNews(title, "root", url);
        return "";
    }
}
