package com.xinder.api.enums;


import org.apache.commons.lang3.StringUtils;

public enum ResultCode {

    SUCCESS("成功", 10001),
    FAIL("失败", 10002),
    PARAM_ERROR("参数错误", 10003),
    DATA_ERROR("数据异常", 10004),

    // 文章
    SENSITIVE_WORD("含有敏感词", 10005),

    // 用户
    LOGIN_SUCCESS("登录成功", 20001),
    LOGIN_FAIL("登录失败", 20002),
    LOGOUT_SUCCESS("退出成功", 20003),
    LOGOUT_FAIL("退出失败", 20004),

    ;

    private String desc;
    private Integer code;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    ResultCode(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public static ResultCode getResultCodeByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        ResultCode[] enumsArray = ResultCode.values();
        for (ResultCode resultCode : enumsArray) {
            if (resultCode.name().equals(name)) {
                return resultCode;
            }
        }
        return null;
    }
}
