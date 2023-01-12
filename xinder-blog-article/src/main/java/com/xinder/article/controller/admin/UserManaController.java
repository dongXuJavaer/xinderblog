package com.xinder.article.controller.admin;

import com.xinder.api.bean.RespBean;
import com.xinder.api.bean.Role;
import com.xinder.api.bean.User;
import com.xinder.article.feign.UserManaFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by sang on 2017/12/24.
 */
@RestController
@RequestMapping("/admin")
public class UserManaController {

//    @Autowired
//    UserServiceImpl userServiceImpl;

    @Autowired
    private UserManaFeignClient userManaFeignClient;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> getUserByNickname(String nickname) {
//        return userServiceImpl.getUserByNickname(nickname);
        return userManaFeignClient.getUserByNickname(nickname);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable Long id) {
//        return userServiceImpl.getUserById(id);
        return userManaFeignClient.getUserById(id);
    }

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public List<Role> getAllRole() {
//        return userServiceImpl.getAllRole();
        return userManaFeignClient.getAllRole();
    }

    @RequestMapping(value = "/user/enabled", method = RequestMethod.PUT)
    public RespBean updateUserEnabled(Boolean enabled, Long uid) {
//        if (userServiceImpl.updateUserEnabled(enabled, uid) == 1) {
//            return new RespBean("success", "更新成功!");
//        } else {
//            return new RespBean("error", "更新失败!");
//        }
        return userManaFeignClient.updateUserEnabled(enabled, uid);
    }

    @RequestMapping(value = "/user/{uid}", method = RequestMethod.DELETE)
    public RespBean deleteUserById(@PathVariable Long uid) {
//        if (userServiceImpl.deleteUserById(uid) == 1) {
//            return new RespBean("success", "删除成功!");
//        } else {
//            return new RespBean("error", "删除失败!");
//        }
        return userManaFeignClient.deleteUserById(uid);
    }

    @RequestMapping(value = "/user/role", method = RequestMethod.PUT)
    public RespBean updateUserRoles(Long[] rids, Long id) {
//        if (userServiceImpl.updateUserRoles(rids, id) == rids.length) {
//            return new RespBean("success", "更新成功!");
//        } else {
//            return new RespBean("error", "更新失败!");
//        }
        return userManaFeignClient.updateUserRoles(rids, id);
    }
}
