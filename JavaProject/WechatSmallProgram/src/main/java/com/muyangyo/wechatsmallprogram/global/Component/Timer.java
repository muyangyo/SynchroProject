package com.muyangyo.wechatsmallprogram.global.Component;

import org.springframework.stereotype.Component;

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
@Component
public class Timer {

    public String getFormatTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }

    public String getYear() {
        return String.valueOf(LocalDate.now().getYear());
    }

    public String getMonth() {
        return String.valueOf(LocalDate.now().getMonthValue());
    }

    public String getDayOfMonth() {
        return String.valueOf(LocalDate.now().getDayOfMonth());
    }

    public String getDayOfWeek() {
        return String.valueOf(LocalDate.now().getDayOfWeek().getValue());
    }
}
