package com.xinder.api.enums;

/**
 * @author Xinder
 * @date 2023-01-11 11:57
 */
public enum  UserEnums {

    CURRENT_USER("currentUser","当前用户"),

    USER_ONLINE_PREFIX_KEY("user:online:","在线用户redis的key前缀"),

    ;


    private String value;
    private String desc;

    UserEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
