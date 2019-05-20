package com.rcircle.service.gateway.services;

import com.alibaba.fastjson.JSON;
import com.rcircle.service.gateway.events.sink.NewsSink;
import com.rcircle.service.gateway.events.sink.SmsSink;
import com.rcircle.service.gateway.model.News;
import com.rcircle.service.gateway.model.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@EnableBinding(value = {NewsSink.class, SmsSink.class})
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private List<News> newsList = new ArrayList<>();
    private List<Sms> smsList = new ArrayList<>();
    private Lock newsListLock = new ReentrantLock();
    private Lock smsListLock = new ReentrantLock();

    @StreamListener(NewsSink.TOPIC)
    public void receiveNews(String string) {
        News news = JSON.parseObject(string, News.class);
        logger.info("Title:{}, author:{}, url:{}, date:{}", news.getTitle(), news.getAuthor(), news.getUrl(), news.getDate());
        newsListLock.lock();
        newsList.add(news);
        newsListLock.unlock();
    }

    @StreamListener(SmsSink.TOPIC)
    public void receiveSms(String str) {
        Sms sms = JSON.parseObject(str, Sms.class);
        logger.info("SEND_UID:{}, RECEIVE_UID:{}, MESSAGE:{}, DATE:{}", sms.getSender_name(), sms.getReceiver_uid(), sms.getMessage(), sms.getDate());
        smsListLock.lock();
        smsList.add(sms);
        smsListLock.unlock();
    }

    public List<News> getNewsList() {
        List<News> currentList = cloneList(newsList, newsListLock);
        if(currentList.isEmpty() || currentList.size() < 2) {
            News defaultNews = new News();
            defaultNews.setTitle("Welcome to Simple Life");
            defaultNews.setUrl("#");
            currentList.add(defaultNews);
            defaultNews = new News();
            defaultNews.setTitle("You can post your opinion");
            defaultNews.setUrl("#");
            currentList.add(defaultNews);
        }
        return currentList;
    }

    public List<Sms> getSmsList() {
        return cloneList(smsList, smsListLock);
    }

    private <T> List<T> cloneList(List<T> list, Lock lock) {
        List<T> newList = new ArrayList<>();
        if (list.size() == 0) {
            return newList;
        }
        lock.lock();
        Iterator<T> iter = list.iterator();
        while (iter.hasNext()) {
            newList.add(iter.next());
        }
//        list.clear();
        lock.unlock();
        return newList;
    }
}
