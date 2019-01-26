package com.ray.service.store.mapper;

import com.ray.service.store.model.Log;
import com.ray.service.store.model.LogDetial;
import com.ray.service.store.model.Reply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMapper {
    public int createLogDetial(LogDetial detial);

    public int createLog(Log log);

    public int createReply(Reply reply);

    public int deleteLog(@Param("id") int lid);

    public int deleteLogDetial(@Param("id") int lid);

    public int deleteReply(@Param("id") int rid);

    public int changeLog(Log log);

    public int changeLogStatus(@Param("id") int id, @Param("status") int status);

    public Log getLogById(@Param("id") int lid);

    public List<Log> getLogsByUid(@Param("uid") int uid);

    public List<Log> getPublicLogsByType(@Param("type") int type);

    public List<Log> getGroupLogsByType(@Param("type") int type, @Param("gid") int gid);
}
