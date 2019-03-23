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
    public int createLogDetial(LogDetail detial);

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

    public List<String> getCategoryId(@Param("uid")int uid);

    public int createCategory(Category category);

    public int addNewCategoryFor(@Param("uid")int uid, @Param("cid")int cid);

    public int deleteCategory(@Param("cid")int cid);

    public int deleteCategoryFor(@Param("uid")int uid, @Param("cid")int cid);


}
