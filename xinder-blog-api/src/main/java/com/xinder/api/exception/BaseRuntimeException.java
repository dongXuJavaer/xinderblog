package com.xinder.api.exception;

/**
 * @author Xinder
 * @date 2023-02-02 17:49
 */
public class BaseRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    public BaseRuntimeException(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public BaseRuntimeException(String message) {
        super();
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
