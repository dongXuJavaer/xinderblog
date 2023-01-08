package com.xinder.article.controller;

import com.xinder.api.abstcontroller.AbstractController;
import com.xinder.api.bean.RespBean;
import com.xinder.api.response.UserDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.rest.UserApi;
import com.xinder.article.service.AuthService;
import com.xinder.article.service.impl.UserServiceImpl;
import com.xinder.article.utils.AuthToken;
import com.xinder.article.utils.CookieUtil;
import com.xinder.article.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by sang on 2017/12/24.
 */
@RestController
public class UserController extends AbstractController implements UserApi {

    @Autowired
    UserServiceImpl userServiceImpl;

    @RequestMapping("/currentUserName")
    public String currentUserName() {
        return Util.getCurrentUser().getNickname();
    }

    @RequestMapping("/currentUserId")
    public Long currentUserId() {
        return Util.getCurrentUser().getId();
    }

    @RequestMapping("/currentUserEmail")
    public String currentUserEmail() {
        return Util.getCurrentUser().getEmail();
    }

    @RequestMapping("/isAdmin")
    public Boolean isAdmin() {
        List<GrantedAuthority> authorities = Util.getCurrentUser().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().contains("超级管理员")) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping(value = "/updateUserEmail", method = RequestMethod.PUT)
    public RespBean updateUserEmail(String email) {
        if (userServiceImpl.updateUserEmail(email) == 1) {
            return new RespBean("success", "开启成功!");
        }
        return new RespBean("error", "开启失败!");
    }


    @Override
    public BaseResponse<UserDtoResult> login(String username, String password, HttpServletResponse response) {
        UserDtoResult userDtoResult = userServiceImpl.login(username, password, response);
        return buildJson(userDtoResult);
    }

    @Override
    public BaseResponse<UserDtoResult> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return BaseResponse.success();
    }
}
