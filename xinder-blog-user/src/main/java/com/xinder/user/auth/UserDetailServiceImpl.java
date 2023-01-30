package com.xinder.user.auth;


import com.xinder.api.bean.Role;
import com.xinder.api.bean.User;
import com.xinder.user.config.MyPasswordEncoder;
import com.xinder.user.mapper.RolesMapper;
import com.xinder.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 自定义认证类，用来查询出用户基本信息
 *
 * @author Xinder
 * @date 2023-01-08 13:34
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Autowired
    private RolesMapper rolesMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 如果使用的是QQ登录，则username传入的实际值是openid
        User user = userMapper.selectByOpenid(username);
        if (user == null) {
            // 不是QQ登录，则是通过用户名登录
            user = userMapper.loadUserByUsername(username);

            if (user == null) {
                //避免返回null，这里返回一个不含有任何值的User对象，在后期的密码比对过程中一样会验证失败
//            return new User();
            }

        }
        redisTemplate.opsForValue().set(user.getUsername(), user, Duration.ofMinutes(30));
        logger.info("用户{}信息存入redis", user.getUsername());

        //查询用户的角色信息，并返回存入user中
        List<Role> roles = rolesMapper.getRolesByUid(user.getId());
        user.setRoles(roles);
        String permissions = buildPermissions(roles);
        String password = user.getPassword();
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(username, password,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));

        return userDetails;
    }

    private String buildPermissions(List<Role> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return "";
        }
        StringBuilder stringBuffer = new StringBuilder();
        roleList.forEach(role -> {
            stringBuffer.append(role.getName()).append(",");
        });
        return stringBuffer.substring(0, stringBuffer.length() - 1);
    }
}
