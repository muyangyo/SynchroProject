package com.muyangyo.wechatsmallprogram.global.model;

import lombok.Data;

import java.util.Date;

@Data
public class Drama {
    private int id;
    private int userId;
    private String dramaName;
    private String coverUrl;
    private String introduction;
    private String madeCompany;
    private String playingPlatform;
    private int isUpdate;
    private int totalNumber;
    private int updateNumber;
    private Date setTime;
    private String updateTime;
    private float love;
    private String remark;
}
