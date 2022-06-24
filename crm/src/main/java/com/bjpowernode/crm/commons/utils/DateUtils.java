package com.bjpowernode.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-04 16:28
 */
public class DateUtils {
    /**
     * 对指定的data对象格式化时间    yyyy-MM-dd HH:mm:ss
     * format写错了,改动太麻烦了,现就这样吧,下面的方法改过来了
     * @param date
     * @return
     */
    public static String formateDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 对指定的data对象格式化时间    yyyy-MM-dd
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 对指定的data对象格式化时间    HH:mm:ss
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }
}
