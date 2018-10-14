package com.ray.service.store.mapper;

import com.ray.service.store.model.LogDetial;
import com.ray.service.store.model.LogFile;
import com.ray.service.store.model.ReplyLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMapper {
    public int createLogDetial(LogDetial log);

    public int createLog(LogFile log);

    public int updateLogDetial(LogDetial detial);

    public int updateLog(LogFile log);

    public int createReply(ReplyLog log);

    public int deleteLog(@Param("id") long id);

    public int deleteLogDetial(@Param("id") long id);

    public int deleteReply(@Param("id") long id);

    public List<LogFile> query(@Param("type") int type, @Param("uid") long uid,
                               @Param("mintime") long minitime, @Param("maxtime") long maxtime);

    public List<LogFile> queryWithoutReply(@Param("type") int type, @Param("uid") long uid,
                                           @Param("mintime") long minitime, @Param("maxtime") long maxtime);

    public LogFile queryLogById(long id);

    public List<LogFile> queryAllLogFile(@Param("uid") long uid, @Param("logid") long logid);

    public List<ReplyLog> queryLogReplyByLogId(long logid);
}
