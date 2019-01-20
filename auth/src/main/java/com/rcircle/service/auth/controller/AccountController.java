package com.rcircle.service.auth.controller;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AccountController {
    @GetMapping("me")
    public Authentication showMe(Authentication authentication) {
        return authentication;
    }
}
