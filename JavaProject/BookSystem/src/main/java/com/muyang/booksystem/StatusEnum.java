package com.muyang.booksystem;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/26
 * Time: 10:18
 */
public enum StatusEnum {
    SUCCESS(1, "success"),
    ERROR(-1, "error");

    private int code;
    private String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsgByCode(int code) {
        switch (code) {
            case 1:
                return StatusEnum.SUCCESS.msg;
            case -1:
                return StatusEnum.ERROR.msg;
            default:
                return "";
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
