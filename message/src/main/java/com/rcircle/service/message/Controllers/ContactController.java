package com.rcircle.service.message.Controllers;

import com.rcircle.service.message.services.SendService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@RequestMapping("/contact")
public class ContactController {
    @Resource
    private SendService sendService;

    @PostMapping("feedback")
    public String sendFeedback(@RequestParam(name = "username") String username,
                               @RequestParam(name = "email") String email,
                               @RequestParam(name = "subject") String subject,
                               @RequestParam(name = "message") String message) {
        sendService.sendFeedBack(username, email, subject, message);
        return "";
    }
}
