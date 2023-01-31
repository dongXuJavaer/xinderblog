package com.xinder.article.controller;

import com.xinder.api.bean.RespBean;
import com.xinder.article.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by sang on 2017/12/24.
 */
@RestController
public class UserController {

//    @Autowired
//    UserServiceImpl userServiceImpl;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserFeignClient userFeignClient;

    @RequestMapping("/currentUserName")
    public String currentUserName() {
//        return Util.getCurrentUser(redisTemplate).getNickname();
        System.out.println("调用这里不行----" + new Date());
        return userFeignClient.currentNickName();
    }

    @RequestMapping("/currentUserId")
    public Long currentUserId() {
//        return Util.getCurrentUser(redisTemplate).getId();
        return userFeignClient.currentUserId();
    }

    @RequestMapping("/currentUserEmail")
    public String currentUserEmail() {
//        return Util.getCurrentUser(redisTemplate).getEmail();
        return userFeignClient.currentUserEmail();
    }

    @RequestMapping("/isAdmin")
    public Boolean isAdmin() {
//        List<GrantedAuthority> authorities = Util.getCurrentUser().getAuthorities();
//        for (GrantedAuthority authority : authorities) {
//            if (authority.getAuthority().contains("超级管理员")) {
//                return true;
//            }
//        }
//        return false;
        return userFeignClient.isAdmin();
    }

    @RequestMapping(value = "/updateUserEmail", method = RequestMethod.PUT)
    public RespBean updateUserEmail(String email) {
//        if (userServiceImpl.updateUserEmail(email) == 1) {
//            return new RespBean("success", "开启成功!");
//        }
//        return new RespBean("error", "开启失败!");

        return userFeignClient.updateUserEmail(email);
    }


}
