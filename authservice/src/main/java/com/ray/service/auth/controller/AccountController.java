package com.ray.service.auth.controller;

import com.ray.service.auth.model.Account;
import com.ray.service.auth.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/auth")
public class AccountController {
    @GetMapping("me")
    public Authentication showMe(Authentication authentication) {
        return authentication;
    }
}
