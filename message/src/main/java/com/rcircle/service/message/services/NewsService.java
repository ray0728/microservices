package com.rcircle.service.message.services;

import com.rcircle.service.message.events.source.NewsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {
    @Autowired
    private NewsBean newsBean;

    public void sendBreakNews(int level, String title, String author, String url) {
        newsBean.sendBreakNews(level, title, author, url);
    }

    public void sendNews(String title, String author, String url){
        newsBean.sendNews(title, author, url);
    }
}
