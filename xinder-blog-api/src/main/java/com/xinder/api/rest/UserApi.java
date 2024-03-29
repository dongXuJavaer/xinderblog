package com.xinder.api.rest;

import com.xinder.api.response.RespBean;
import com.xinder.api.request.UserDtoReq;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Xinder
 * @date 2023-01-08 10:54
 */
@Api(tags = "UserApi")
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
     *
     * @return
     */
    @PostMapping("/logout")
    BaseResponse<DtoResult> logout();


    /**
     * 前端用户根据id查看其他人信息
     *
     * @return
     */
    @ApiOperation(value = "查看他人个人主页", notes = "查看他人个人主页", tags = {"UserApi"})
    @GetMapping("/{uid}")
    BaseResponse<UserDtoSimpleResult> getUserByIdFront(@PathVariable("uid") Long uid);

    /**
     * 修改个人信息
     *
     * @return
     */
    @ApiOperation(value = "修改个人信息", notes = "修改个人信息", tags = {"UserApi"})
    @PostMapping("/update")
    BaseResponse<Result> updateUserInfo(@RequestBody UserDtoReq userDtoReq);

    /**
     * 上传头像
     *
     * @return
     */
    @ApiOperation(value = "上传头像", notes = "上传头像", tags = {"UserApi"})
    @PostMapping("/upload/head")
    BaseResponse<String> uploadHeadImg(@RequestParam("file") MultipartFile file);

    /**
     * 获取用户总数
     *
     * @return
     */
    @ApiOperation(value = "获取用户总数", notes = "获取用户总数", tags = {"UserApi"})
    @PostMapping("/num")
    BaseResponse<DtoResult> userCount();

    @ApiOperation(value = "获取当前响应的domain", notes = "获取当前响应的domain", tags = {"UserApi"})
    @RequestMapping(value = "/domain", method = RequestMethod.GET)
    String getCookieDomain();

    @ApiOperation(value = "注册", notes = "注册", tags = {"UserApi"})
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    BaseResponse<Result> register(@RequestBody UserDtoReq userDtoReq);

    @ApiOperation(value = "绑定QQ", notes = "绑定QQ", tags = {"UserApi"})
    @RequestMapping(value = "/bind/qq", method = RequestMethod.GET)
    BaseResponse<Result> bindQQ(@RequestParam(value = "access_token", required = false) String accessToken);

    @ApiOperation(value = "取消绑定QQ", notes = "取消绑定QQ", tags = {"UserApi"})
    @RequestMapping(value = "/cancel/qq/{uid}", method = RequestMethod.POST)
    BaseResponse<Result> cancelQQ(@PathVariable("uid") Long uid);

    @ApiOperation(value = "修改密码", notes = "修改密码", tags = {"ArticleController"})
    @RequestMapping(value = "/update-pwd", method = RequestMethod.POST)
    BaseResponse<Result> updatePwd(@RequestBody Map map);

}
