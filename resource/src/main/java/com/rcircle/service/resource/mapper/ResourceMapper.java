package com.rcircle.service.resource.mapper;

import com.rcircle.service.resource.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ResourceMapper {
    public int createLogDetail(LogDetail detial);

    public int createLog(Log log);

    public int createReply(Reply reply);

    public int deleteLog(@Param("id") int lid);

    public int deleteLogDetial(@Param("id") int lid);

    public int deleteReply(@Param("id") int rid);

    public int changeLog(Log log);

    public int getLogStatus(@Param("id") int id);

    public int changeLogStatus(@Param("id") int id, @Param("status") int status);

    public Log getLogById(@Param("id") int lid);

    public List<Log> getLogs(@Param("uid") int uid, @Param("type") int type,
                             @Param("tid") int tid,
                             @Param("gid") int gid, @Param("title") String title,
                             @Param("status") int status, @Param("offset") int offset,
                             @Param("count") int count);

    public int getLogCount(Map<String, Object> params);

    public List<Reply> getLogReplies(@Param("id") int lid);

    public Reply getReply(@Param("id") int id);

    public List<Category> getAllCategory(@Param("uid") int uid);

    public List<Category> getAllPublicCategory();

    public int createCategory(Category category);

    public int addUserDefCategory(Category category);

    public int deleteCategory(@Param("cid") int cid, @Param("desc") String desc);

    public int deleteUserDefCategory(@Param("id") int id, @Param("uid") int uid);

    public Category getAccountCategoryMapId(Category category);

    public Category getCategory(Map<String, Object> params);

    public int changeLogDetail(@Param("id") int id, @Param("log") String log, @Param("res_url") String res_url);

    public List<Tag> getTags(@Param("id") int id);

    public List<Log> getTopLogs();

    public int createTag(Tag log);

    public int addTagForLog(@Param("lid") int lid, Tag tag);

    public int deleteTagFromLog(@Param("id") int id, @Param("tid") int tid, @Param("lid") int lid);

    public List<Tag> getAllTags(@Param("uid") int uid);

    public Tag getTag(@Param("desc") String desc);
//    public String getCategory(@Param("id")int id);


}
