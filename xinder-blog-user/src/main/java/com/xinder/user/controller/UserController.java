package com.xinder.user.controller;

import com.xinder.api.response.RespBean;
import com.xinder.api.enums.ResultCode;
import com.xinder.api.request.UserDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.api.rest.UserApi;
import com.xinder.common.util.FileUtils;
import com.xinder.common.util.TokenDecode;
import com.xinder.common.util.Util;
import com.xinder.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by sang on 2017/12/24.
 */
@RestController
@RequestMapping("/user")
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
     * @param accessToken 请求到QQ官方的
     * @param response    响应
     * @return 用户信息
     */
    @RequestMapping("/login/qq")
    public BaseResponse<UserDtoResult> qqLogin(
            @RequestParam(value = "access_token", required = false) String accessToken,
            HttpServletResponse response) {

        UserDtoResult userDtoResult = userServiceImpl.qqLogin(accessToken, response);
        return buildJson(userDtoResult);
    }

    /**
     * 注销
     *
     * @return 注销结果
     */
    @Override
    public BaseResponse<DtoResult> logout() {
        DtoResult dtoResult = userServiceImpl.logout();
        return buildJson(dtoResult);
    }

    /**
     * 根据用户id获取
     *
     * @param uid 用户id
     * @return {@link BaseResponse}<{@link UserDtoSimpleResult}>
     */
    @Override
    public BaseResponse<UserDtoSimpleResult> getUserByIdFront(@PathVariable("uid") Long uid) {
        UserDtoSimpleResult simpleResult = userServiceImpl.getUserByIdFront(uid);
        return buildJson(simpleResult);
    }

    @Override
    public BaseResponse<Result> updateUserInfo(@RequestBody UserDtoReq userDtoReq) {
        Result result = userServiceImpl.updateUserInfo(userDtoReq);
        return buildJson(result);
    }

    @Override
    public BaseResponse<String> uploadHeadImg(@RequestParam("file") MultipartFile file) {
        String url = null;
        try {
            url = FileUtils.upload(file, FileUtils.FOLDER_USER_HEAD);
        } catch (Exception e) {
            e.printStackTrace();
            return buildJson(ResultCode.FAIL.getCode(), ResultCode.FAIL.getDesc());
        }
        return buildJson(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc(), url);
    }

    @Override
    public BaseResponse<DtoResult> userCount() {
        DtoResult dtoResult = userServiceImpl.getUserCount();
        return buildJson(dtoResult);
    }

    @Override
    public String getCookieDomain() {
        return userServiceImpl.getCookieDomain();
    }

    @Override
    public BaseResponse<Result> register(UserDtoReq userDtoReq) {
        Result result = userServiceImpl.register(userDtoReq);
        return buildJson(result);
    }

    @Override
    public BaseResponse<Result> bindQQ(String token) {
        Result result = userServiceImpl.bindQQ(token);
        return buildJson(result);
    }

    @Override
    public BaseResponse<Result> cancelQQ(Long uid) {
        Result result = userServiceImpl.cancelQQ(uid);
        return buildJson(result);
    }

    @Override
    public BaseResponse<Result> updatePwd(Map map) {
        Result result = userServiceImpl.updatePwd(map);
        return buildJson(result);
    }
}
