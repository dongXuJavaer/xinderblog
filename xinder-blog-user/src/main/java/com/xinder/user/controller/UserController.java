package com.xinder.user.controller;

import com.xinder.api.bean.RespBean;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.api.rest.UserApi;
import com.xinder.common.util.Util;
import com.xinder.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by sang on 2017/12/24.
 */
@RestController
public class UserController extends AbstractController implements UserApi {

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/currentUserName")
    public String currentUserName() {
        return Util.getCurrentUser(redisTemplate).getNickname();
    }

    @RequestMapping("/currentUserId")
    public Long currentUserId() {
        return Util.getCurrentUser(redisTemplate).getId();
    }

    @RequestMapping("/currentUserEmail")
    public String currentUserEmail() {
        return Util.getCurrentUser(redisTemplate).getEmail();
    }

    @RequestMapping("/isAdmin")
    public Boolean isAdmin() {
//        List<GrantedAuthority> authorities = Util.getCurrentUser().getAuthorities();
//        for (GrantedAuthority authority : authorities) {
//            if (authority.getAuthority().contains("超级管理员")) {
//                return true;
//            }
//        }
//        return false;
        return true;
    }

    @RequestMapping(value = "/updateUserEmail", method = RequestMethod.PUT)
    public RespBean updateUserEmail(String email) {
        if (userServiceImpl.updateUserEmail(email) == 1) {
            return new RespBean("success", "开启成功!");
        }
        return new RespBean("error", "开启失败!");
    }


    @RequestMapping("login1")
    public BaseResponse<UserDtoResult> login(
            String username,
            String password,
            HttpServletResponse response) {

        UserDtoResult userDtoResult = userServiceImpl.login(username, password, response);
        return buildJson(userDtoResult);
    }

    public BaseResponse<UserDtoResult> logout() {
//        HttpSession session = request.getSession();
//        session.invalidate();
        return BaseResponse.success();
    }
}
