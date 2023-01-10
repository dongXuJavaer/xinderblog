package com.xinder.article.service.impl;

import com.xinder.api.bean.Role;
import com.xinder.api.bean.User;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.article.mapper.RolesMapper;
import com.xinder.article.mapper.UserMapper;
import com.xinder.article.service.AuthService;
import com.xinder.article.service.UserService;
import com.xinder.article.util.AuthToken;
import com.xinder.article.util.CookieUtils;
import com.xinder.article.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by sang on 2017/12/17.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RolesMapper rolesMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthService authService;

    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;

    //秘钥
    @Value("${auth.clientSecret}")
    private String clientSecret;

    //Cookie存储的域名
    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    //Cookie生命周期
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;


    /**
     * @param user
     * @return 0表示成功
     * 1表示用户名重复
     * 2表示失败
     */
    public int reg(User user) {
        User loadUserByUsername = userMapper.loadUserByUsername(user.getUsername());
        if (loadUserByUsername != null) {
            return 1;
        }
        //插入用户,插入之前先对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(1);//用户可用
        long result = userMapper.reg(user);
        //配置用户的角色，默认都是普通用户
        String[] roles = new String[]{"2"};
        int i = rolesMapper.addRoles(roles, user.getId());
        boolean b = i == roles.length && result == 1;
        if (b) {
            return 0;
        } else {
            return 2;
        }
    }

    public int updateUserEmail(String email) {
        return userMapper.updateUserEmail(email, Util.getCurrentUser().getId());
    }

    public List<User> getUserByNickname(String nickname) {
        List<User> list = userMapper.getUserByNickname(nickname);
        return list;
    }

    public List<Role> getAllRole() {
        return userMapper.getAllRole();
    }

    public int updateUserEnabled(Boolean enabled, Long uid) {
        return userMapper.updateUserEnabled(enabled, uid);
    }

    public int deleteUserById(Long uid) {
        return userMapper.deleteUserById(uid);
    }

    public int updateUserRoles(Long[] rids, Long id) {
        int i = userMapper.deleteUserRolesByUid(id);
        return userMapper.setUserRoles(rids, id);
    }

    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    @Override
    public UserDtoResult login(String username, String password, HttpServletResponse response) {
        UserDtoResult userDtoResult = (UserDtoResult) DtoResult.DataDtoFail(UserDtoResult.class);
        //判断用户是否为空
        if (StringUtils.isEmpty(username)) {
            userDtoResult.setMsg("用户名不能为空");
            return userDtoResult;
        }
        //判断密码是否为空
        if (StringUtils.isEmpty(password)) {
            userDtoResult.setMsg("密码不能为空");
            return userDtoResult;
        }

        AuthToken authToken = authService.login(username, password, clientId, clientSecret);
        CookieUtils.addCookie(response, cookieDomain,
                "/", "Authorization", authToken.getAccessToken(), cookieMaxAge, false);

        userDtoResult = (UserDtoResult) DtoResult.DataDtoSuccess(UserDtoResult.class);

        return userDtoResult;
    }
}
