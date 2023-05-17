package com.xinder.common.exception;

import com.xinder.api.enums.ResultCode;
import com.xinder.api.exception.BaseRuntimeException;

/**
 * 权限校验异常
 * @author Xinder
 * @date 2023-02-02 16:32
 */
public class CheckAuthException extends BaseRuntimeException {

    public CheckAuthException(Integer code, String message) {
        super(code, message);
    }

    public CheckAuthException(ResultCode resultCode) {
        super(resultCode.getCode(), resultCode.getDesc());
    }

    public CheckAuthException(String message) {
        super(message);
    }
}
