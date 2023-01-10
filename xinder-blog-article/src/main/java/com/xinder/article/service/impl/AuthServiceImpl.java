package com.xinder.article.service.impl;

import com.xinder.article.service.AuthService;
import com.xinder.article.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 用户认证服务
 * @author Xinder
 * @date 2023-01-08 11:13
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret) {


        return null;
    }
}
