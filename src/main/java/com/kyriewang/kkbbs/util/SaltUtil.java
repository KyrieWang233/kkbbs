package com.kyriewang.kkbbs.util;

import java.util.Random;

public class SaltUtil {
    public static String getSalt(int n){
        String s="0123456789";
        for(char a='A';a<='Z';a++)
            s+=a;
        for(char a='a';a<='z';a++)
            s+=a;
        String res="";
        for(int i=0;i<n;i++){
            res+=s.charAt(new Random().nextInt(s.length()-1));
        }
        return res;
    }

}
