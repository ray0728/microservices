package com.rcircle.service.resource.mapper;

import com.rcircle.service.resource.model.Category;
import com.rcircle.service.resource.model.Log;
import com.rcircle.service.resource.model.LogDetail;
import com.rcircle.service.resource.model.Reply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMapper {
    public int createLogDetail(LogDetail detial);

    public int createLog(Log log);

    public int createReply(Reply reply);

    public int deleteLog(@Param("id") int lid);

    public int deleteLogDetial(@Param("id") int lid);

    public int deleteReply(@Param("id") int rid);

    public int changeLog(Log log);

    public int getLogStatus(@Param("id")int id);

    public int changeLogStatus(@Param("id") int id, @Param("status") int status);

    public Log getLogById(@Param("id") int lid);

    public List<Log> getLogsByUid(@Param("uid") int uid);

    public List<Log> getPublicLogsByType(@Param("type") int type);

    public List<Log> getGroupLogsByType(@Param("type") int type, @Param("gid") int gid);

    public List<Reply> getLogReplies(@Param("id")int lid);

    public List<Category> getAllCategory(@Param("uid")int uid);

    public int createCategory(Category category);

    public int addUserDefCategory(Category category);

    public int deleteCategory(@Param("cid")int cid, @Param("desc")String desc);

    public int deleteUserDefCategory(@Param("id")int id, @Param("uid")int uid);

    public int getAccountCategoryMapId(Category category);

    public Category getCategory(@Param("desc")String desc);

    public int changeLogDetail(@Param("id")int id, @Param("log")String log, @Param("res_url")String res_url);

//    public String getCategory(@Param("id")int id);


}
