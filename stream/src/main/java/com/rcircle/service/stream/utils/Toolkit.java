package com.rcircle.service.stream.utils;

public class Toolkit {
    public static String encodeForUrl(String str) {
        str = str.replace("+", "%2B");
        str = str.replaceAll(" ", "%20");
        str = str.replaceAll("/", "%2F");
        str = str.replace("?", "%3F");
        str = str.replaceAll("%", "%25");
        str = str.replaceAll("#", "%23");
        str = str.replaceAll("&", "%26");
        str = str.replaceAll("=", "%3D");
        return str;
    }

    public static String decodeFromUrl(String str) {
        str = str.replace("%2B", "+");
        str = str.replaceAll("%20", " ");
        str = str.replaceAll("%2F", "/");
        str = str.replace("%3F", "?");
        str = str.replaceAll("%25", "%");
        str = str.replaceAll("%23", "#");
        str = str.replaceAll("%26", "&");
        str = str.replaceAll("%3D", "=");
        return str;
    }
}
