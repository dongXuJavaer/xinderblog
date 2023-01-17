package com.xinder.user.service.impl;

import com.xinder.api.bean.Role;
import com.xinder.api.bean.User;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.common.util.TokenDecode;
import com.xinder.user.mapper.RolesMapper;
import com.xinder.user.mapper.UserMapper;
import com.xinder.common.util.AuthToken;
import com.xinder.common.util.CookieUtils;
import com.xinder.common.util.Util;
import com.xinder.user.service.AuthService;
import com.xinder.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate redisTemplate;

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

    @Autowired
    private TokenDecode tokenDecode;


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
        return userMapper.updateUserEmail(email, Util.getCurrentUser(tokenDecode, redisTemplate).getId());
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
        UserDtoResult userDtoResult = (UserDtoResult) DtoResult.dataDtoFail(UserDtoResult.class);
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

        User user = userMapper.loadUserByUsername(username);
        userDtoResult = DtoResult.dataDtoSuccess(UserDtoResult.class);
        BeanUtils.copyProperties(user, userDtoResult);

        return userDtoResult;
    }
}