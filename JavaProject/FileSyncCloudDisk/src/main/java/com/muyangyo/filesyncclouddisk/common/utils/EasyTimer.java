package com.muyangyo.filesyncclouddisk.common.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    public String getFormatTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当前年份。
     *
     * @return 当前年份的字符串表示
     */
    public String getYear() {
        return String.valueOf(LocalDate.now().getYear());
    }

    /**
     * 获取当前月份。
     *
     * @return 当前月份的字符串表示
     */
    public String getMonth() {
        return String.valueOf(LocalDate.now().getMonthValue());
    }

    /**
     * 获取当前日期中的日。
     *
     * @return 当前日期中的日的字符串表示
     */
    public String getDayOfMonth() {
        return String.valueOf(LocalDate.now().getDayOfMonth());
    }

    /**
     * 获取当前日期是星期几。
     *
     * @return 当前日期是星期几的数字表示（1 = 星期一, 7 = 星期天）
     */
    public String getDayOfWeek() {
        return String.valueOf(LocalDate.now().getDayOfWeek().getValue());
    }
}

