package com.rcircle.service.auth.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Password {
    public static String crypt(String str) {
        return new BCryptPasswordEncoder().encode(str.trim());
    }
}
