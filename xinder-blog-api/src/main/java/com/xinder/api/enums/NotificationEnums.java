package com.xinder.api.enums;

public enum NotificationEnums {

    SYSTEM(1, "系统通知"),
    FOLLOW(2, "关注通知"),
    COMMENTS(3, "帖子评论通知"),
    UP(4, "帖子点赞通知"),

    ;

    private int code;
    private String desc;


    NotificationEnums(int code, String desc) {
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
