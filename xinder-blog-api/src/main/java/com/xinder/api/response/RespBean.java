package com.xinder.api.response;

import lombok.Data;

/**
 * Created by sang on 2017/12/17.
 */
@Data
public class RespBean {
    private String status;
    private String msg;

    public RespBean() {
    }

    public RespBean(String status, String msg) {

        this.status = status;
        this.msg = msg;
    }
}
