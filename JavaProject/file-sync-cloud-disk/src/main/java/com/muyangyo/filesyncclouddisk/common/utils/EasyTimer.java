package com.muyangyo.filesyncclouddisk.common.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/4
 * Time: 9:40
 */
public class EasyTimer {

    /**
     * 获取当前时间的格式化字符串，格式为 "yyyy-MM-dd hh:mm:ss"。
     *
     * @return 当前时间的格式化字符串
     */
    public String getNowFormatTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当前年份。
     *
     * @return 当前年份的字符串表示
     */
    public String getNowYear() {
        return String.valueOf(LocalDate.now().getYear());
    }

    /**
     * 获取当前月份。
     *
     * @return 当前月份的字符串表示
     */
    public String getNowMonth() {
        return String.valueOf(LocalDate.now().getMonthValue());
    }

    /**
     * 获取当前日期中的日。
     *
     * @return 当前日期中的日的字符串表示
     */
    public String getNowDayOfMonth() {
        return String.valueOf(LocalDate.now().getDayOfMonth());
    }

    /**
     * 获取当前日期是星期几。
     *
     * @return 当前日期是星期几的数字表示（1 = 星期一, 7 = 星期天）
     */
    public String getNowDayOfWeek() {
        return String.valueOf(LocalDate.now().getDayOfWeek().getValue());
    }


    /**
     * 获取指定时间的格式化字符串，格式为 "yyyy-MM-dd hh:mm:ss"
     *
     * @param date 指定的时间
     * @return 指定时间的格式化字符串
     */
    public static String getFormatTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取指定时间的格式化字符串，格式为 "yyyy-MM-dd HH:mm:ss"
     *
     * @param dateTime 指定的时间
     * @return 指定时间的格式化字符串
     */
    public static String getFormatTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    /**
     * 获取指定时间的年份
     *
     * @param date 指定的时间
     * @return 指定时间的年份的字符串表示
     */
    public static String getYear(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return String.valueOf(localDate.getYear());
    }

    /**
     * 获取指定时间的月份
     *
     * @param date 指定的时间
     * @return 指定时间的月份的字符串表示
     */
    public static String getMonth(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return String.valueOf(localDate.getMonthValue());
    }

    /**
     * 获取指定时间中的日
     *
     * @param date 指定的时间
     * @return 指定时间中的日的字符串表示
     */
    public static String getDayOfMonth(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return String.valueOf(localDate.getDayOfMonth());
    }

    /**
     * 获取指定时间是星期几
     *
     * @param date 指定的时间
     * @return 指定时间是星期几的数字表示（1 = 星期一, 7 = 星期天）
     */
    public static String getDayOfWeek(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return String.valueOf(localDate.getDayOfWeek().getValue());
    }
}

