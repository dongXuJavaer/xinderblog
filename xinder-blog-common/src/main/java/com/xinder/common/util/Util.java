package com.xinder.common.util;

import com.alibaba.fastjson.JSON;
import com.xinder.api.bean.History;
import com.xinder.api.bean.User;
import com.xinder.api.enums.UserEnums;
import com.xinder.common.constant.CommonConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
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
            redisTemplate.opsForValue().set(userKey, user, 60, TimeUnit.MINUTES);
        } else {
            user = new User();
        }
        return user;

    }

    /**
     * 从cookie中获取用户的浏览历史记录
     *
     * @return {@link List}<{@link History}>
     */
    public static List<History> getHistoryListByCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        String name = CommonConstant.HTTP_HEADER_HISTORY;

        List<History> historyList = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    String value = cookie.getValue();
                    String decode = null;
                    try {
                        decode = URLDecoder.decode(value, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    List<History> list = JSON.parseArray(decode, History.class);
                    if (list != null) {
                        historyList.addAll(list);
                    }
                }
            }
        }
        return historyList;
    }
}
