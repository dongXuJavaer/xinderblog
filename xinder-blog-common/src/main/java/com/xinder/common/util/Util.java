package com.xinder.common.util;

import com.xinder.api.bean.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by sang on 2017/12/20.
 */
public class Util {

    public static User getCurrentUser(TokenDecode tokenDecode, RedisTemplate redisTemplate) {

//        org.springframework.security.core.userdetails.User userDetailService =
//                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, String> userInfo = tokenDecode.getUserInfo();
        String username = userInfo.get("user_name");

        User user = (User) redisTemplate.opsForValue().get(username);
        if (user != null) {
            redisTemplate.opsForValue().set(username, user, 30, TimeUnit.MINUTES);
        } else {
            user = new User();
        }
        return user;

    }


}
