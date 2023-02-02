package com.xinder.user.exception;

import com.xinder.api.enums.ResultCode;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.result.Result;
import com.xinder.common.abstcontroller.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 全局异常处理
 *
 * @author Xinder
 * @date 2023-02-02 16:31
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends AbstractController {

    /**
     * 无权限时的异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    //指定出现什么异常执行这个方法
    @ExceptionHandler(value = {CheckAuthException.class})
    public BaseResponse<Result> checkException(CheckAuthException e) {
        log.error("异常信息：{}", e.getMessage());
        Result result = Result.fail(ResultCode.FAIL.getCode(), "没有权限");
        return buildJson(result);
    }
}
