package com.xinder.common.util;

import com.xinder.api.bean.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by sang on 2017/12/20.
 */
public class Util {

    public static User getCurrentUser(RedisTemplate redisTemplate) {

//        org.springframework.security.core.userdetails.User userDetailService =
//                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (o == null || (o instanceof String)){
            return new User();
        }

        UserDetails userDetails = (UserDetails) o;
        User user = (User) redisTemplate.opsForValue().get(userDetails.getUsername());

//        if (RequestContextHolder.getRequestAttributes() != null) {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            HttpSession session = request.getSession();
//            Object currentUser = session.getAttribute("currentUser");
//            return (User) currentUser;
//        }
        return user;
    }


}
