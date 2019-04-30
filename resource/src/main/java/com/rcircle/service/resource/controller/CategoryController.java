package com.rcircle.service.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.resource.model.Account;
import com.rcircle.service.resource.model.Category;
import com.rcircle.service.resource.service.AccountService;
import com.rcircle.service.resource.service.ResourceService;
import com.rcircle.service.resource.utils.ErrInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/cate")
public class CategoryController {
    @Resource
    private ResourceService resourceService;
    @Resource
    private AccountService accountService;

    private Account getOpAccount(Principal principal) {
        return accountService.loadUser(0, principal.getName());
    }

    @GetMapping("/list")
    public String getAllCategorys(Principal principal){
        List list = null;
        if(principal != null) {
            Account op = getOpAccount(principal);
            if (op != null) {
                list = resourceService.getAllCategory(op.getUid());
            }
        }
        if(list == null){
            list = resourceService.getAllCategory(0);
        }
        return JSONObject.toJSONString(list);
    }

    @PostMapping("/new")
    public String addNewCategory(Principal principal,
                                 @RequestParam(name = "desc", required = true) String desc){
        Account op = getOpAccount(principal);
        if (op == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_CREATE_NEW_CATEGORY, "Invalid request parameters.");
        }
        Category category = resourceService.createNewCategory(op.getUid(), desc);
        return JSONObject.toJSONString(category);
    }

    @DeleteMapping("/del")
    public String deleteCategory(Principal principal,
                                 @RequestParam(name = "id") int id){
        Account op = getOpAccount(principal);
        if (op == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_DELETE_CATEGORY, "Invalid request parameters.");
        }
        resourceService.deleteUserDefCategory(op.getUid(), id);
        return "";
    }

    @PutMapping("/update")
    public String update(Principal principal,
                         @RequestParam(name = "id") int id,
                         @RequestParam(name = "desc") String desc){
        Account op = getOpAccount(principal);
        if (op == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_UPDATE_CATEGORY, "Invalid request parameters.");
        }
        int line = resourceService.deleteUserDefCategory(op.getUid(), id);
        if(line == 0){
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_UPDATE_CATEGORY, "Invalid request parameters.");
        }
        Category category = resourceService.createNewCategory(op.getUid(), desc);
        return JSONObject.toJSONString(category);
    }
}
