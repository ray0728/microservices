package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.model.Account;
import com.rcircle.service.gateway.services.AccountService;
import com.rcircle.service.gateway.services.ResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController {
    @Resource
    AccountService accountService;
    @Resource
    private ResourceService resourceService;


    @GetMapping(value = {"", "home"})
    public String showHomePage(Principal principal, ModelMap mm) {
        mm.addAttribute("title", "RC - LifeStyle - Blog");
        mm.addAttribute("categories", resourceService.getAllCategoryForCurrentUser());
        mm.addAttribute("logs", resourceService.getAllDiaries(0, 0, null, 0, 0, 5));
        return "index";
    }


    @GetMapping("join")
    public String signup(ModelMap mm) {
        mm.addAttribute("title", "Join RC");
        mm.addAttribute("account", new Account());
        return "sign_up";
    }

    @PostMapping("join")
    public String createNewAccount(@ModelAttribute Account account, ModelMap mm) {
        String ret = accountService.createAccount(account);
        if (ret.startsWith("failed")) {
            mm.addAttribute("errinfo", ret);
            return "sign_up";
        }
        return "redirect:/home/";
    }

    @GetMapping("login")
    public String login(ModelMap mm) {
        mm.addAttribute("title", "Login");
        return "login";
    }
}
