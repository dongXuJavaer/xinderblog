package com.xinder.common.util;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dong
 * @since 2022-09-07 09:19
 */
@Data
public class AuthToken implements Serializable {
    /**
     * 令牌信息
     */
    String accessToken;

    /**
     * 刷新token(refresh_token)
     */
    String refreshToken;

    /**
     * jwt短令牌
     */
    //
    String jti;

    /**
     * 登录失败时的错误类型
     */
    String error;
    /**
     * 错误描述
     */
    String errorDescription;

}
