package com.ray.service.account.redis;

import com.ray.service.account.util.SpringContextHolder;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RedisCache implements Cache {
    private String id;
    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    public RedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("RedisCache Need NonNull ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        if (key != null) {
            redisTemplate.opsForValue().set(key.toString(), value);
        }
    }

    @Override
    public Object getObject(Object key) {
        Object ret = null;
        if(key != null){
            ret = redisTemplate.opsForValue().get(key.toString());
        }
        return ret;
    }

    @Override
    public Object removeObject(Object key) {
        Object ret = getObject(key);
        if(ret != null){
            redisTemplate.delete(key.toString());
        }
        return null;
    }

    @Override
    public void clear() {
    }

    @Override
    public int getSize() {
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        int size = Integer.valueOf(redisConnection.dbSize().toString());
        redisConnection.close();
        return size;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
