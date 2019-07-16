package com.rcircle.service.message.Controllers;

import com.rcircle.service.message.services.SendService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;

@RestController
@RequestMapping("/sms")
public class SmsController {
    @Resource
    private SendService smsService;

    @PostMapping("/general")
    public String sendSms(Principal principal,
                          @RequestParam(name = "uid") int uid,
                          @RequestParam(name = "msg") String msg) {
        smsService.sendSms(0, "root", uid, msg);
        return "";
    }

    @PostMapping("/broadcast")
    public String sendBroadcast(Principal principal,
                                @RequestParam(name = "msg") String msg) {
        smsService.sendBroadcast(msg);
        return "";
    }
}
