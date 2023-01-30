package com.xinder.api.enums;

/**
 * @author Xinder
 * @date 2023-01-30 12:59
 */
public enum QQLoginEnums {

    APP_ID("102037068","APP ID"),
    APP_KEY("2ygAPzOK4buyqVwB","APP KEY"),

    ACCESS_TOKEN_URL("https://graph.qq.com/oauth2.0/token", "请求Token"),

    OPENID_URL("https://graph.qq.com/oauth2.0/me", "获取OPENID"),

    USERINFO_URL("https://graph.qq.com/user/get_user_info", "获取用户信息"),


    // ids
    UNION_ID("unionid", "unionid"),
    OPEN_ID("openid", "openid"),
    CLIENT_ID("client_id", "client_id"),


    ;


    private String value;
    private String desc;

    QQLoginEnums(String value, String desc) {
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

