package com.xinder.api.rest;

import com.xinder.api.bean.RespBean;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.base.BaseResponse;
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
public interface UserApi {

    @RequestMapping("/currentUserName")
    public String currentUserName();

    @RequestMapping("/currentUserId")
    public Long currentUserId();

    @RequestMapping("/currentUserEmail")
    public String currentUserEmail();

    @RequestMapping("/isAdmin")
    public Boolean isAdmin();

    @RequestMapping(value = "/updateUserEmail", method = RequestMethod.PUT)
    RespBean updateUserEmail(String email);

    //退出
    @PostMapping("/logout")
    BaseResponse<UserDtoResult> logout();
}
