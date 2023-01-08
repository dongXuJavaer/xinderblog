package com.xinder.article.auth;

import com.xinder.api.bean.Role;
import com.xinder.api.bean.User;
import com.xinder.article.mapper.RolesMapper;
import com.xinder.article.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 自定义认证类，用来查询出用户基本信息
 *
 * @author Xinder
 * @date 2023-01-08 13:34
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private RolesMapper rolesMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(username);
        if (user == null) {
            //避免返回null，这里返回一个不含有任何值的User对象，在后期的密码比对过程中一样会验证失败
            return new User();
        }

        //查询用户的角色信息，并返回存入user中
        List<Role> roles = rolesMapper.getRolesByUid(user.getId());
//        user.setRoles(roles);
        String permissions = buildPermissions(roles);
        String password = user.getPassword();
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(username, password,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
        return userDetails;
    }

    private String buildPermissions(List<Role> roleList) {
        StringBuilder stringBuffer = new StringBuilder();
        roleList.forEach(role -> {
            stringBuffer.append(roleList).append(",");
        });
        stringBuffer.substring(0, stringBuffer.length() - 1);
        return stringBuffer.toString();
    }
}
