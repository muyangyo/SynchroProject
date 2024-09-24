package com.muyangyo.wechatsmallprogram.global.model.httpModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/24
 * Time: 17:36
 */
@Data
public class WXResponse {
    @JsonProperty("errcode")
    private Integer errCode;
    @JsonProperty("session_key")
    private String sessionKey;
    @JsonProperty("openid")
    private String openId;
    @JsonProperty("errmsg")
    private String errMsg;
}

