package com.xinder.api.rest;

import com.xinder.api.bean.RespBean;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    BaseResponse<DtoResult> logout();


    /**
     * 前端用户根据id查看其他人信息
     * @return
     */
    @ApiOperation(value = "查看他人个人主页", notes = "查看他人个人主页", tags = {"UserApi"})
    @GetMapping("/{uid}")
    BaseResponse<UserDtoSimpleResult> getUserByIdFront(@PathVariable("uid") Long uid);
}
