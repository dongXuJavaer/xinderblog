package com.xinder.api.rest;

import com.xinder.api.bean.RespBean;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Xinder
 * @date 2023-01-08 10:54
 */
@Api(tags = "UserController")
@RequestMapping("/user")
public interface UserApi {

    @RequestMapping("/currentUserName")
    String currentNickName();

    @RequestMapping("/currentUser")
    BaseResponse<UserDtoResult> currentUser();

    @RequestMapping("/currentUserId")
    public Long currentUserId();

    @RequestMapping("/currentUserEmail")
    public String currentUserEmail();

    @RequestMapping("/isAdmin")
    public Boolean isAdmin();

    @RequestMapping(value = "/updateUserEmail", method = RequestMethod.PUT)
    RespBean updateUserEmail(String email);

//    @PostMapping("/login")
//     BaseResponse<UserDtoResult> login(
//            String username,
//            String password,
//            HttpServletResponse response);

    //退出
    @PostMapping("/logout")
    BaseResponse<DtoResult> logout();
}
