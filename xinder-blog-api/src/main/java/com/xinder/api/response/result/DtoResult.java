package com.xinder.api.response.result;

import com.xinder.api.enums.ResultCode;

import java.io.Serializable;

public class DtoResult implements Serializable {

    private static final long serialVersionUID = 745645633555365L;

    private Integer code;
    private String msg;
    private Object data;

    public DtoResult() {
        super();
    }

    public DtoResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public DtoResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static DtoResult success() {
        return new DtoResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc());
    }

    public static DtoResult fail(ResultCode resultCode) {
        return new DtoResult(resultCode.getCode(), resultCode.getDesc());
    }

    public static DtoResult fail(Integer code, String msg) {
        return new DtoResult(code, msg);
    }

    public static <T extends DtoResult> T dataDtoSuccess(Class<T> c) {
        T dtoResult = null;
        try {
            dtoResult = (T) c.newInstance();
            dtoResult.setCode(ResultCode.SUCCESS.getCode());
            dtoResult.setMsg(ResultCode.SUCCESS.getDesc());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return dtoResult;
    }


    public static  <T extends DtoResult> T dataDtoFail(Class<T> c) {
        T dtoResult = null;
        try {
            dtoResult = (T) c.newInstance();
            dtoResult.setCode(ResultCode.FAIL.getCode());
            dtoResult.setMsg(ResultCode.FAIL.getDesc());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return dtoResult;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
