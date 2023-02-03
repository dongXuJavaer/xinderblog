package com.xinder.user.service;

import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.result.DtoResult;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Xinder
 * @date 2023-01-08 11:23
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param response
     * @return
     */
    UserDtoResult login(String username, String password, HttpServletResponse response);

    /**
     * qq登录
     *
     * @param token
     * @return
     */
    UserDtoResult qqLogin(String token, HttpServletResponse response);

    /**
     * 从Cookie中获取当前登录用户信息
     *
     * @return UserDtoResult
     */
    UserDtoResult getCurrentUser();

    /**
     * 从Cookie中获取当前登录用户信息
     *
     * @return UserDtoResult
     */
    DtoResult logout();


    /**
     * 前台用户，查询其他人用户信息
     * @param id
     * @return
     */
    UserDtoSimpleResult getUserByIdFront(Long id);


}
