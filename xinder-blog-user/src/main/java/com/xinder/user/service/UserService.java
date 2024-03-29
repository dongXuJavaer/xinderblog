package com.xinder.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.User;
import com.xinder.api.request.UserDtoReq;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Xinder
 * @date 2023-01-08 11:23
 */
public interface UserService extends IService<User> {

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
     *
     * @param id
     * @return
     */
    UserDtoSimpleResult getUserByIdFront(Long id);

    /**
     * 修改用户信息
     *
     * @param userDtoReq
     * @return
     */
    Result updateUserInfo(UserDtoReq userDtoReq);

    /**
     * 修改头像
     *
     * @param file
     * @return
     */
    Result uploadHeadImg(MultipartFile file);

    /**
     * 获取用户总数
     *
     * @return
     */
    DtoResult getUserCount();

    /**
     * 获取domain
     */
    /**
     * 获取用户总数
     *
     * @return
     */
    String getCookieDomain();

    /**
     * 注册
     *
     * @param userDtoReq 用户dto请求
     * @return {@link Result}
     */
    Result register(UserDtoReq userDtoReq);

    /**
     * 绑定qq
     *
     * @return {@link Result}
     */
    Result bindQQ(String token);

    /**
     * 取消qq绑定
     *
     * @param uid uid
     * @return {@link Result}
     */
    Result cancelQQ(Long uid);

    /**
     * 更新密码
     *
     * @param map oldPassword
     *            newPassword1
     *            newPassword2
     * @return {@link Result}
     */
    Result updatePwd(Map map);
}
