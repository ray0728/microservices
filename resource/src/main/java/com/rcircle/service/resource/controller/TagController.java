package com.rcircle.service.resource.controller;

import com.alibaba.fastjson.JSON;
import com.rcircle.service.resource.model.Account;
import com.rcircle.service.resource.model.ResultData;
import com.rcircle.service.resource.model.Tag;
import com.rcircle.service.resource.service.AccountService;
import com.rcircle.service.resource.service.ResourceService;
import com.rcircle.service.resource.utils.ResultInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tag/")
public class TagController {
    @Resource
    private ResourceService resourceService;
    @Resource
    private AccountService accountService;

    private Account getOpAccount(Principal principal) {
        return accountService.loadUser(0, principal.getName());
    }

    @GetMapping("list")
    public String getAllTags(Principal principal) {
        int uid = 0;
        if (principal != null) {
            Account op = getOpAccount(principal);
            if (op != null) {
                uid = op.getUid();
            }
        }
        List<Tag> tagList = resourceService.getAllTags(uid);
        ResultData data = new ResultData();
        data.setType(ResultInfo.translate(ResultInfo.ErrType.SUCCESS));
        data.addToMap("list_tag", tagList);
        return JSON.toJSONString(data);
    }
}
