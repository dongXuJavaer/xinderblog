package com.xinder.user.config.handler.login;

import com.alibaba.fastjson.JSONObject;
import com.xinder.api.bean.User;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.result.DtoResult;
import com.xinder.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author Xinder
 * @date 2023-01-08 13:18
 */
@Component
@RefreshScope
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${appname.user}")
    private String name;

    @Value("${spring.redis.host}")
    private String redisUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        //获取用户信息存放到Redis中
        String username = authentication.getName();
        User user = userMapper.loadUserByUsername(username);

        redisTemplate.opsForValue().set(username, user, TimeUnit.MINUTES.toMillis(30), TimeUnit.MINUTES);
        logger.info("【AuthenticationSuccessHandler.onAuthenticationSuccess】" +
                "用户：{} 登录成功",  ((User)redisTemplate.opsForValue().get(username)).getUsername());

        System.out.println("name:" + name + "-----------------------");
        System.out.println("redis:" + redisUrl + "-----------------------");


        response.setContentType("application/json;charset=utf-8");

        UserDtoResult userDtoResult = buildUserDtoResult(user);
        BaseResponse result = BaseResponse.success();
        result.setData(userDtoResult);
        PrintWriter out = response.getWriter();
        // 使用JSONObject转json会出错
        out.write(JSONObject.toJSONString(result));

//        ObjectMapper objectMapper = new ObjectMapper();
//        out.write(objectMapper.writeValueAsString(result));

        out.flush();
        out.close();
    }

    private UserDtoResult buildUserDtoResult(User user) {
        UserDtoResult userDtoResult = DtoResult.dataDtoSuccess(UserDtoResult.class);
        BeanUtils.copyProperties(user, userDtoResult);
        return userDtoResult;
    }
}
