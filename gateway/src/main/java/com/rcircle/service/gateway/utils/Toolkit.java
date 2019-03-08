package com.rcircle.service.gateway.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class Toolkit {
    private static final char[] up_alpha_letter = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final char[] down_alpha_letter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static String randomString(int length) {
        Random random = new Random();
        String ret = "";
        for (int i = 0; i < length; i++) {
            switch (random.nextInt(3)) {
                case 0:
                    ret += up_alpha_letter[random.nextInt(up_alpha_letter.length)];
                    break;
                case 1:
                    ret += down_alpha_letter[random.nextInt(down_alpha_letter.length)];
                    break;
                case 2:
                    ret += random.nextInt(10);

            }
        }
        return ret;
    }

    public static String getLocalIP() {
        InetAddress address = null;
        String ipaddr = null;
        try {
            address = InetAddress.getLocalHost();
            ipaddr = address.getHostAddress();
        } catch (UnknownHostException e) {
            ipaddr = "127.0.0.1";
        }
        return ipaddr;
    }

    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }


}
