package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.model.Account;
import com.rcircle.service.gateway.services.AccountService;
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

    @GetMapping("")
    public String showHomePage(Principal principal, ModelMap mm) {
        if (principal != null) {
            mm.addAttribute("title", "RC - Welcome," + principal.getName());
        } else {
            mm.addAttribute("title", "RC - Welcome");
        }
        return "index";
    }

    @GetMapping("securedPage")
    public String showSecuredPage() {
        return "securePage";
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
        mm.clear();
        mm.addAttribute("title", "RC - Welcome");
        return "index";
    }

    @GetMapping("login")
    public String login(ModelMap mm) {
        mm.addAttribute("title", "Login");
        return "login";
    }
}
