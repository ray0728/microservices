package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.model.Account;
import com.rcircle.service.gateway.services.AccountService;
import com.rcircle.service.gateway.services.ReferenceService;
import com.rcircle.service.gateway.services.ResourceService;
import com.rcircle.service.gateway.utils.MvcToolkit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController {
    @Resource
    AccountService accountService;
    @Resource
    ReferenceService referenceService;
    @Resource
    private ResourceService resourceService;


    @GetMapping(value = {"", "home"})
    public String showHomePage(Principal principal, ModelMap mm) {
        MvcToolkit.autoLoadTopMenuData(resourceService, mm);
        MvcToolkit.autoLoadSideBarData(resourceService, mm);
        mm.addAttribute("title", "- Simple Lift -");
        mm.addAttribute("logs", resourceService.getAllBlogs(0, 0, null, 0, 0, 5));
        return "index";
    }

    @GetMapping("join")
    public String signup(ModelMap mm) {
        MvcToolkit.autoLoadTopMenuData(resourceService, mm);
        MvcToolkit.autoLoadSideBarData(resourceService, mm);
        mm.addAttribute("title", "Create new account");
        mm.addAttribute("account", new Account());
        mm.addAttribute("quot", referenceService.getRandomQuotation());
        return "sign_up";
    }

    @PostMapping("join")
    public String createNewAccount(@ModelAttribute Account account, ModelMap mm) {
        String ret = accountService.createAccount(account);
        if (!ret.startsWith("success")) {
            mm.addAttribute("errinfo", ret);
            mm.addAttribute("quot", referenceService.getRandomQuotation());
            mm.addAttribute("title", "Create new account");
            return "sign_up";
        }
        return "redirect:/home/";
    }

    @GetMapping("login")
    public String login(ModelMap mm, @RequestParam(name = "info", required = false, defaultValue = "") String msg) {
        MvcToolkit.autoLoadTopMenuData(resourceService, mm);
        MvcToolkit.autoLoadSideBarData(resourceService, mm);
        mm.addAttribute("title", "Login");
        if (!msg.isEmpty()) {
            mm.addAttribute("msg", msg);
        }
        return "login";
    }
}
