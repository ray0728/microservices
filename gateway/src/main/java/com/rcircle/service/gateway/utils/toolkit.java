package com.rcircle.service.gateway.utils;

import java.util.Random;

public class toolkit {
    private static final char[] up_alpha_letter = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private static final char[] down_alpha_letter = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public static String randomString(int length){
        Random random = new Random();
        String ret = "";
        for(int i = 0; i < length; i++){
            switch (random.nextInt(3)){
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

}
