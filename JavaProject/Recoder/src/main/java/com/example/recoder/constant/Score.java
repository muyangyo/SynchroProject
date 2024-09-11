package com.example.recoder.constant;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class Score {
    private Integer id;
    private Long score;
    private Date createTime;

    public String getCreateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(createTime);
    }
}