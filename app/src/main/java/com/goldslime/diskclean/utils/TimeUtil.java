package com.goldslime.diskclean.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

public class TimeUtil {

    public static long getCurrentUnxTimeMilliseconds() {
        return System.currentTimeMillis();
    }

    //用户数据库保存数据
    public static String getCurrentTime() {
        long timeSeconds = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        return sdf.format(timeSeconds);
    }

    //用户数据库保存数据
    public static String getCurrentMilTime() {
        long timeSeconds = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

        return sdf.format(timeSeconds);
    }

    public static String generateFileName() {
        long timeSeconds = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());

        return sdf.format(timeSeconds);
    }

    public static String formatTime(long timeSeconds) {
        if (timeSeconds == 0) {
            return "0";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        return sdf.format(timeSeconds);
    }

    static int seed = 0;

    public static String mockTime() {
        seed += (30 + new Random().nextInt(240));//分钟
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        //(3)获取当前系统时间的前1秒种的时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);//设置参数时间
        calendar.add(Calendar.DAY_OF_MONTH, -120);
        calendar.add(Calendar.MINUTE, seed);
        date = calendar.getTime();
        return sdf.format(date);
    }

    public static String getSettingTime() {
        long timeMillis = System.currentTimeMillis();

        SimpleDateFormat sdf;

        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

        return sdf.format(timeMillis);
    }

    //time格式 2021-05-11 12:30:33
    //把时间转换成各种语言对应的时间
    public static String trans(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeMillis = date.getTime();

        SimpleDateFormat sdf;

        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

        return sdf.format(timeMillis);
    }
}
