package com.xinder.api.rest.admin;

import com.xinder.api.bean.RespBean;
import com.xinder.api.bean.Role;
import com.xinder.api.bean.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-01-12 19:21
 */
public interface UserManaApi {


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    List<User> getUserByNickname(String nickname);

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    User getUserById(@PathVariable Long id);

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    List<Role> getAllRole();

    @RequestMapping(value = "/user/enabled", method = RequestMethod.PUT)
    RespBean updateUserEnabled(Boolean enabled, Long uid);

    @RequestMapping(value = "/user/{uid}", method = RequestMethod.DELETE)
    RespBean deleteUserById(@PathVariable Long uid);

    @RequestMapping(value = "/user/role", method = RequestMethod.PUT)
    RespBean updateUserRoles(Long[] rids, Long id);
}
