package com.rcircle.service.account.controller;

import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.account.model.Account;
import com.rcircle.service.account.model.Group;
import com.rcircle.service.account.model.GroupMemberMap;
import com.rcircle.service.account.service.AccountService;
import com.rcircle.service.account.service.GroupService;
import com.rcircle.service.account.util.ResultInfo;
import com.rcircle.service.account.util.SimpleDate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Resource
    private GroupService groupService;
    @Resource
    private AccountService accountService;

    private boolean isAdminOp(String name, int admin_uid) {
        Account opAccount = accountService.getAccount(name, 0);
        if (opAccount == null) {
            return false;
        }
        return opAccount.getUid() == admin_uid;
    }

    @PostMapping("create")
    public String create(Principal principal, @RequestParam(name = "name", required = true) String name,
                         @RequestParam(name = "desc", required = true) String desc,
                         @RequestParam(name = "type", required = true) int type) {
        if (principal == null || name == null || name.length() == 0 || desc == null || desc.length() == 0 || type == 0) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_CREATE_GROUP, "Invalid request parameters.");
        }
        Account opAccount = accountService.getAccount(principal.getName(), 0);
        if (opAccount == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_CREATE_GROUP, "Invalid request parameters.");
        }
        Group group = new Group();
        group.setName(name);
        group.setType(type);
        group.setDesc(desc);
        group.setCreate_date(SimpleDate.getUTCTime());
        group.setLevel(1);
        group.setAdmin_uid(opAccount.getUid());
        if (groupService.create(group) > 0) {
            return JSONObject.toJSONString(group);
        } else {
            return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_CREATE_GROUP, "Invalid request.");
        }
    }

    @DeleteMapping("delete")
    public String delete(Principal principal, @RequestParam(name = "gid", required = true) int gid) {
        if (principal == null || gid == 0) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_DELETE_GROUP, "Invalid request parameters.");
        }
        Group group = groupService.getDetialById(gid);
        if (!isAdminOp(principal.getName(), group.getAdmin_uid())) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_DELETE_GROUP, "Invalid request parameters.");
        }
        if (groupService.delete(group) != 1) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_DELETE_GROUP, "Invalid request parameters.");
        }
        return "";
    }

    @PutMapping("change")
    public String changeInfo(Principal principal, @RequestParam(name = "gid", required = true) int gid,
                             @RequestParam(name = "name", required = false, defaultValue = "") String name,
                             @RequestParam(name = "desc", required = false, defaultValue = "") String desc,
                             @RequestParam(name = "type", required = false, defaultValue = "0") int type,
                             @RequestParam(name = "uid", required = false, defaultValue = "0") int admin_uid) {
        if (principal == null || gid == 0) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_CHANGE_GROUP, "Invalid request parameters.");
        }
        Group group = groupService.getDetialById(gid);
        if (!isAdminOp(principal.getName(), group.getAdmin_uid())) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_CHANGE_GROUP, "Invalid request parameters.");
        }
        boolean shouldUpdate = false;
        if (!group.getName().equals(name)) {
            group.setName(name);
            shouldUpdate = true;
        }
        if (!group.getDesc().equals(desc)) {
            group.setDesc(desc);
            shouldUpdate = true;
        }
        if (group.getType() != type) {
            group.setType(type);
            shouldUpdate = true;
        }
        if (group.getAdmin_uid() != admin_uid) {
            group.setAdmin_uid(admin_uid);
            shouldUpdate = true;
        }
        if (shouldUpdate) {
            groupService.changeInfo(group);
        }
        return JSONObject.toJSONString(group);
    }

    private boolean checkMembers(Group group, int[] members, boolean isExist) {
        boolean ret = true;
        boolean isFound = false;
        for (GroupMemberMap gmm : group.getMember()) {
            isFound = false;
            for (int uid : members) {
                if (uid == gmm.getUid()) {
                    isFound = true;
                    break;
                }
            }
            if (isFound == isExist) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    @PutMapping("changeMember")
    public String changeMember(Principal principal, @RequestParam(name = "gid", required = true) int gid,
                               @RequestParam(name = "add", required = false, defaultValue = "") int[] newMembers,
                               @RequestParam(name = "rm", required = false, defaultValue = "") int[] rmMembers) {
        if (principal == null || gid == 0) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_CHANGE_GROUP_MEMBER, "Invalid request parameters.");
        }
        Group group = groupService.getDetialById(gid);
        if (!isAdminOp(principal.getName(), group.getAdmin_uid())) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_CHANGE_GROUP_MEMBER, "Invalid request parameters.");
        }
        if (newMembers.length > 0) {
            if (!checkMembers(group, newMembers, true)) {
                return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_CHANGE_GROUP_MEMBER, "Invalid request parameters.");
            }
            for(int uid:newMembers) {
                groupService.addMember(group, uid);
            }
        }
        if (rmMembers.length > 0) {
            if (!checkMembers(group, rmMembers, true)) {
                return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_CHANGE_GROUP_MEMBER, "Invalid request parameters.");
            }
            for(int uid:rmMembers) {
                groupService.removeMember(group, uid);
            }
        }
        return JSONObject.toJSONString(group);
    }

    @GetMapping("info")
    public String getDetial(Principal principal, @RequestParam(name="gid", required = false, defaultValue = "0")int gid,
                            @RequestParam(name="keyword", required = false, defaultValue = "")String keyword){
        if (principal == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_QUERY_GROUP, "Invalid request parameters.");
        }
        Account opAccount = accountService.getAccount(principal.getName(), 0);
        if (opAccount == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.PARAMS, ResultInfo.CODE_QUERY_GROUP, "Invalid request parameters.");
        }
        List<Group> groups = null;
        Group group = null;
        if(gid > 0){
            group = groupService.getDetialById(gid);
        }else if(!keyword.isEmpty()){
            groups = groupService.getDetialByKeyWord(keyword);
        }else{
            groups = groupService.getDetialByUid(opAccount.getUid());
        }
        if(groups == null){
            groups = new LinkedList<>();
            groups.add(group);
        }
        return JSONObject.toJSONString(groups);
    }
}
