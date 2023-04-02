package com.xinder.api.enums;

public enum PointEnums {

    RETUCE(0,"减少积分"),
    ADD(1,"增加积分"),

    ;
    private int code;
    private String desc;


    PointEnums(int code, String desc) {
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
