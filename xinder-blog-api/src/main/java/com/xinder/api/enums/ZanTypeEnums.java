package com.xinder.api.enums;

/**
 * 帖子点赞状态
 */
public enum ZanTypeEnums {

    CANCEL(0,"未点赞"),
    ZAN(1,"已点赞")

    ;
    private Integer code;
    private String desc;


    ZanTypeEnums(Integer code, String desc) {
        this.code = code;
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

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
