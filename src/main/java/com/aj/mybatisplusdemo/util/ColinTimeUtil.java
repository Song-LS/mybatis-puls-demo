package com.aj.mybatisplusdemo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Aj
 * @date 2018/5/23 13:47
 * 时间格式工具类
 **/
public class ColinTimeUtil {

    private static final String FORMAT1 = "yyyy-MM-dd";
    private static final String FORMAT2 = "yyyy-MM-dd HH:mm:ss";
    private static final String FORMAT3 = "HH:mm";

    public static String toTextNYR(Object o) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT1);
        return dateFormat.format(o);
    }

    public static Date toDateNYR(String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT1);
        return dateFormat.parse(time);
    }

    public static String toTextNYRSFM(Object o) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT2);
        return dateFormat.format(o);
    }

    public static Date toDateNYRSFM(String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT2);
        return dateFormat.parse(time);
    }

    public static String toTextSF(Object o) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT3);
        return dateFormat.format(o);
    }

    public static Date toDateSF(String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT3);
        return dateFormat.parse(time);
    }
}
