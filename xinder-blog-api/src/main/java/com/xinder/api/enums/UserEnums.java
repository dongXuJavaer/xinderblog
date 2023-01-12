package com.xinder.api.enums;

import lombok.Data;

/**
 * @author Xinder
 * @date 2023-01-11 11:57
 */
public enum  UserEnums {

    CURRENT_USER("currentUser","当前用户")
    ;


    private String status;
    private String desc;

    UserEnums(String status, String desc) {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
