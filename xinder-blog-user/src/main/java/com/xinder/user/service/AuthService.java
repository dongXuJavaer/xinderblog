package com.xinder.user.service;

import com.xinder.common.util.AuthToken;
import org.springframework.stereotype.Service;

/**
 * 认证服务接口
 *
 * @author Xinder
 * @date 2023-01-08 11:13
 */
@Service
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

    /**
     * 授权认证
     * @param username
     * @return
     */
    String createJwtToken(String username);
}
