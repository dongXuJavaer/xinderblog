package com.xinder.api.enums;

/**
 * 通知类型枚举
 *
 * @author Xinder
 * @date 2023-03-28 16:09
 */
public enum NotificationTypeEnums {


    SYSTEM(1,"系统通知"),
    FOLLOW(2,"关注通知"),
    CCOMMENTS(3,"关注通知"),
    ZAN(4,"关注通知"),

    ;
    private int code;
    private String desc;


    NotificationTypeEnums(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
