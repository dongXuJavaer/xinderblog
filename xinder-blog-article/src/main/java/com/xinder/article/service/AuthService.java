package com.xinder.article.service;

import com.xinder.article.utils.AuthToken;

/**
 * 认证服务接口
 *
 * @author Xinder
 * @date 2023-01-08 11:13
 */
public interface AuthService {

    /**
     * 授权认证
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    AuthToken login(String username, String password, String clientId, String clientSecret);
}
