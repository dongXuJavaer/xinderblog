package com.xinder.api.rest;

import com.xinder.api.response.UserDtoResult;
import com.xinder.api.response.base.BaseResponse;
import io.swagger.annotations.Api;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Xinder
 * @date 2023-01-08 10:54
 */
@Api(tags = "UserController")
public interface UserApi {

    //登录方法
    @PostMapping("/login")
    BaseResponse<UserDtoResult> login(String username, String password, HttpServletResponse response);

    //退出
    @PostMapping("/logout")
    BaseResponse<UserDtoResult> logout(HttpServletRequest request);
}
