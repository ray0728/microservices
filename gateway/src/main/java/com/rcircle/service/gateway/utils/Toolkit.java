package com.rcircle.service.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.rcircle.service.gateway.model.ResultData;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Toolkit {
    private static final char[] up_alpha_letter = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final char[] down_alpha_letter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final String[] month_map = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    private static final String regEx_html = "<[^>]+>";
    private static final String regEx_space = "\\s*|\t|\r|\n";

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
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }


    public static int getYearFom(long localDate) {
        if (isValidDate(localDate)) {
            return Integer.parseInt(String.valueOf(localDate).substring(0, 4));
        } else {
            return 2016;
        }
    }

    public static String getMonthFrom(long localDate) {
        if (isValidDate(localDate)) {
            return month_map[Integer.parseInt(String.valueOf(localDate).substring(4, 6)) - 1];
        } else {
            return month_map[6];
        }
    }

    private static boolean isValidDate(long date) {
        String dateStr = String.valueOf(date);
        if (dateStr.length() < 8) {
            return false;
        }
        if (Integer.parseInt(dateStr.substring(4, 6)) >= month_map.length) {
            return false;
        }

        if (Integer.parseInt(dateStr.substring(6, 8)) > 31) {
            return false;
        }
        return true;
    }

    public static int getDayFrom(long localDate) {
        if (isValidDate(localDate)) {
            return Integer.parseInt(String.valueOf(localDate).substring(6, 8));
        } else {
            return 28;
        }
    }

    private static String delHTMLTag(String htmlStr, String regex) {
        Pattern p_script = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        return m_script.replaceAll("");
    }

    public static String getTextFromHtml(String htmlStr) {
        htmlStr = delHTMLTag(htmlStr, regEx_script);
        htmlStr = delHTMLTag(htmlStr, regEx_style);
        htmlStr = delHTMLTag(htmlStr, regEx_html);
        htmlStr = delHTMLTag(htmlStr, regEx_space);

        return htmlStr.replaceAll("&nbsp;", "");
    }

    public static <T> int copy(List<T> src, List<T> dst) {
        if (src == null) {
            return 0;
        }
        if (dst == null) {
            dst = new ArrayList<T>();
        }
        Iterator<T> iter = src.iterator();
        while (iter.hasNext()) {
            dst.add(iter.next());
        }
        return src.size();
    }

    public static boolean parseResultData(String ret, Map<String, Object> result) {
        Object newobj;
        if (ret == null || ret.isEmpty()) {
            return false;
        }
        ResultData data = JSON.parseObject(ret, ResultData.class);
        if (!data.isSuccess()) {
            return false;
        }
        Set<Map.Entry<String, Object>> set = result.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            if (entry.getKey().startsWith("list_")) {
                newobj = JSON.parseArray(data.getMap().get(entry.getKey()).toString(), (Class) entry.getValue());
            } else if (isATOMClass((Class) entry.getValue())) {
                newobj = data.getMap().get(entry.getKey());
            } else {
                newobj = JSON.parseObject(data.getMap().get(entry.getKey()).toString(), (Class) entry.getValue());
            }
            result.put(entry.getKey(), newobj);
        }
        return true;
    }

    private static boolean isATOMClass(Class clzss) {
        return clzss.equals(Integer.class);
    }


}
