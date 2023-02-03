package com.xinder.user.controller;

import com.xinder.api.bean.RespBean;
import com.xinder.api.bean.User;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.api.rest.UserApi;
import com.xinder.common.util.TokenDecode;
import com.xinder.common.util.Util;
import com.xinder.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by sang on 2017/12/24.
 */
@RestController
public class UserController extends AbstractController implements UserApi {

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String currentNickName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String property = System.getProperty("spring.security.strategy");
        return Util.getCurrentUser(tokenDecode, redisTemplate).getNickname();
    }

    @Override
    public BaseResponse<UserDtoResult> currentUser() {
        UserDtoResult userDtoResult = userServiceImpl.getCurrentUser();
        return buildJson(userDtoResult);
    }

    @RequestMapping("/currentUserId")
    public Long currentUserId() {
        return Util.getCurrentUser(tokenDecode, redisTemplate).getId();
    }

    @RequestMapping("/currentUserEmail")
    public String currentUserEmail() {
        return Util.getCurrentUser(tokenDecode, redisTemplate).getEmail();
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


    @RequestMapping("/login")
    public BaseResponse<UserDtoResult> login(
            String username,
            String password,
            HttpServletResponse response) {

        UserDtoResult userDtoResult = userServiceImpl.login(username, password, response);
        return buildJson(userDtoResult);
    }

    /**
     * qq登录
     *
     * @param accessToken
     * @param response
     * @return
     */
    @RequestMapping("/login/qq")
    public BaseResponse<UserDtoResult> qqLogin(
            @RequestParam(value = "access_token", required = false) String accessToken,
            HttpServletResponse response) {

        UserDtoResult userDtoResult = userServiceImpl.qqLogin(accessToken, response);
        return buildJson(userDtoResult);
    }

    public BaseResponse<DtoResult> logout() {
        DtoResult dtoResult = userServiceImpl.logout();
        return buildJson(dtoResult);
    }

    @Override
    public BaseResponse<UserDtoSimpleResult> getUserByIdFront(Long uid) {
        UserDtoSimpleResult simpleResult = userServiceImpl.getUserByIdFront(uid);
        return buildJson(simpleResult);
    }
}
