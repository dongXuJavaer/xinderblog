package com.xinder.user.controller.admin;

import com.xinder.api.bean.RespBean;
import com.xinder.api.bean.Role;
import com.xinder.api.bean.User;
import com.xinder.api.rest.admin.UserManaApi;
import com.xinder.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by sang on 2017/12/24.
 */
@RestController
public class UserManaController implements UserManaApi {

    @Autowired
    UserServiceImpl userServiceImpl;

//    @PreAuthorize("hasAuthority('管理员')")
    @Override
    public List<User> getUserByNickname(@RequestParam(value = "nickname", required = false) String nickname) {
        return userServiceImpl.getUserByNickname(nickname);
    }

    @Override
    public User getUserById(@PathVariable Long id) {
        return userServiceImpl.getUserById(id);
    }

    @Override
    public List<Role> getAllRole() {
        return userServiceImpl.getAllRole();
    }

    @Override
    public RespBean updateUserEnabled(Boolean enabled, Long uid) {
        if (userServiceImpl.updateUserEnabled(enabled, uid) == 1) {
            return new RespBean("success", "更新成功!");
        } else {
            return new RespBean("error", "更新失败!");
        }
    }

    @Override
    public RespBean deleteUserById(@PathVariable Long uid) {
        if (userServiceImpl.deleteUserById(uid) == 1) {
            return new RespBean("success", "删除成功!");
        } else {
            return new RespBean("error", "删除失败!");
        }
    }

    @Override
    public RespBean updateUserRoles(Long[] rids, Long id) {
        if (userServiceImpl.updateUserRoles(rids, id) == rids.length) {
            return new RespBean("success", "更新成功!");
        } else {
            return new RespBean("error", "更新失败!");
        }
    }
}
