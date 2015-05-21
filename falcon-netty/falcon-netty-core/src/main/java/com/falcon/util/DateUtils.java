package com.falcon.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fanshuai on 15/5/20.
 */
public class DateUtils {
    private static final SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static long getCurTimeStamp(){
        String time = date.format(new Date());
        return Long.decode(time);
    }
    public static void main(String [] args){
        System.out.println("l"+getCurTimeStamp());
    }
}
