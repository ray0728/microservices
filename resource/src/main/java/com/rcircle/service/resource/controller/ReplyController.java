package com.rcircle.service.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.resource.model.Account;
import com.rcircle.service.resource.model.Log;
import com.rcircle.service.resource.model.Reply;
import com.rcircle.service.resource.model.ResultData;
import com.rcircle.service.resource.service.AccountService;
import com.rcircle.service.resource.service.ResourceService;
import com.rcircle.service.resource.utils.ResultInfo;
import com.rcircle.service.resource.utils.SimpleDate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/reply/")
public class ReplyController {
    @Resource
    private ResourceService resourceService;
    @Resource
    private AccountService accountService;

    private Account getOpAccount(Principal principal) {
        return accountService.loadUser(0, principal.getName());
    }

    @PostMapping("new")
    public String createNewReply(Principal principal,
                                 @RequestParam(name = "id", required = true) int id,
                                 @RequestParam(name="name", required = false, defaultValue = "")String username,
                                 @RequestParam(name="email", required = false, defaultValue = "")String email,
                                 @RequestParam(name = "message", required = true) String msg) {
        int uid = 0;
        if (principal != null) {
            Account account = getOpAccount(principal);
            if (account != null) {
                uid = account.getUid();
            }
        }
        if (uid == 0 && username.isEmpty()) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_CREATE_REPLY, "Invalid request parameters.");
        }
        Log log = resourceService.getLog(id);
        if(log == null){
            return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_CREATE_REPLY, "Invalid request parameters.");
        }
        Reply reply = new Reply();
        reply.setDate(SimpleDate.getUTCTime());
        reply.setDesc(msg);
        reply.setLid(id);
        reply.setUid(uid);
        reply.setUsername(username);
        reply.setEmail(email);
        resourceService.createReply(log, reply);
        return JSONObject.toJSONString(reply);
    }

    @DeleteMapping("delete")
    public String deleteReply(Principal principal, int lid, int rid) {
        Account account = getOpAccount(principal);
        Log log = resourceService.getLog(lid);
        if (account == null || log == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_DELETE_REPLY, "Invalid request parameters.");
        }
        Reply reply = resourceService.getReplyById(rid);
        if(reply.getLid() == log.getId() && account.getUid() == log.getUid()){
            resourceService.deleteReply(log, reply);
        }
        return "";
    }

    @GetMapping("list")
    public String getAllReply(@RequestParam(name = "id", required = true) int id) {
        List<Reply> replies = resourceService.getReplies(id);
        Iterator<Reply> iter = replies.iterator();
        while(iter.hasNext()){
            Reply reply  = iter.next();
            if(reply.getUid() != 0){
                Account account = accountService.loadUser(reply.getUid(), null);
                if(account != null){
                    reply.setUsername(account.getUsername());
                }else if(reply.getUsername().isEmpty() ){
                    reply.setUsername("Guest");
                }
            }
        }
        ResultData data = new ResultData();
        data.setType(ResultInfo.translate(ResultInfo.ErrType.SUCCESS));
        data.addToMap("list_reply", replies);
        return JSONObject.toJSONString(data);
    }
}
