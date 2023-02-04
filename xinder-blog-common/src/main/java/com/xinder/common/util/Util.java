package com.xinder.common.util;

import com.xinder.api.bean.User;
import com.xinder.api.enums.UserEnums;
import com.xinder.common.constant.CommonConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by xinder on 2017/12/20.
 */
public class Util {

    public static User getCurrentUser(TokenDecode tokenDecode, RedisTemplate redisTemplate) {

//        org.springframework.security.core.userdetails.User userDetailService =
//                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = context.getAuthentication();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Object o = authentication.getPrincipal();

        Map<String, Object> userInfo = tokenDecode.getUserInfo();
        String username = (String) userInfo.get(CommonConstant.TOKEN_USERNAME);
        String userKey = UserEnums.USER_ONLINE_PREFIX_KEY.getValue() + username;
        User user = (User) redisTemplate.opsForValue().get(userKey);
        if (user != null) {
            redisTemplate.opsForValue().set(userKey, user, 30, TimeUnit.MINUTES);
        } else {
            user = new User();
        }
        return user;

    }


}
