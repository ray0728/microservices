package com.rcircle.service.message.events.source;

import com.rcircle.service.message.events.models.BreakNews;
import com.rcircle.service.message.events.models.News;
import com.rcircle.service.message.utils.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class NewsBean {
    private NewsSource source;

    @Autowired
    public NewsBean(NewsSource source) {
        this.source = source;
    }

    public void sendBreakNews(int level, String title, String author, String url) {
        BreakNews breakNews = new BreakNews();
        breakNews.setLevel(level);
        breakNews.setTitle(title);
        breakNews.setUrl(url);
        breakNews.setAuthor(author);
        breakNews.setDate(SimpleDate.getUTCTime());
        source.output().send(MessageBuilder.withPayload(breakNews).build());
    }

    public void sendNews(String title, String author, String url){
        News news = new News();
        news.setAuthor(author);
        news.setDate(SimpleDate.getUTCTime());
        news.setTitle(title);
        news.setUrl(url);
        source.output().send(MessageBuilder.withPayload(news).build());
    }
}
