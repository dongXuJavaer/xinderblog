package com.xinder.api.enums;

import lombok.Data;

/**
 * @author Xinder
 * @date 2023-01-06 19:43
 */
public enum PermissionsEnums {

    ADMINISTRATORS(1, "管理员"),
    USER(2, "普通用户");

    private int code;
    private String value;


    PermissionsEnums(int code, String value) {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
