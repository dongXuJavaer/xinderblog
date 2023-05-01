package com.xinder.api.enums;

import java.util.EnumSet;

public enum NotificationEnums {

    SYSTEM(1, "系统通知"),
    FOLLOW(2, "关注通知"),
    COMMENTS(3, "帖子评论通知"),
    ZAN(4, "帖子点赞通知"),
    REPLY(5, "评论回复通知"),

    ;

    private Integer code;
    private String desc;


    NotificationEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
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

    public static boolean judgeType(Integer type) {
        EnumSet<NotificationEnums> notificationEnumsEnumSet = EnumSet.allOf(NotificationEnums.class);
        return notificationEnumsEnumSet.stream().anyMatch(item -> item.getCode().equals(type));
    }
}
