package com.xinder.article.service;

import com.xinder.api.response.dto.UserDtoResult;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Xinder
 * @date 2023-01-08 11:23
 */
public interface UserService {

    /**
     * 用户登录
     * @param username
     * @param password
     * @param response
     * @return
     */
    UserDtoResult login(String username, String password, HttpServletResponse response);
}
