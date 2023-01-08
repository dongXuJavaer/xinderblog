package com.xinder.article.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinder.api.bean.User;
import com.xinder.api.response.UserDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.result.DtoResult;
import com.xinder.article.mapper.UserMapper;
import com.xinder.article.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Xinder
 * @date 2023-01-08 13:18
 */
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final static Logger logger= LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        //获取用户信息存放到session中
        String username = authentication.getName();
        User user = userMapper.loadUserByUsername(username);
        HttpSession session = request.getSession();

        System.out.println(session.getAttribute("currentUser"));

        session.setAttribute("currentUser", user);
        response.setContentType("application/json;charset=utf-8");

        UserDtoResult userDtoResult = buildUserDtoResult(user);
        BaseResponse result = BaseResponse.success();
        result.setData(userDtoResult);
        PrintWriter out = response.getWriter();
        // 使用JSONObject转json会出错
//        out.write(JSONObject.toJSONString(result));
        ObjectMapper objectMapper = new ObjectMapper();
        out.write(objectMapper.writeValueAsString(result));
        out.flush();
        out.close();
    }

    private UserDtoResult buildUserDtoResult(User user) {
        UserDtoResult userDtoResult = DtoResult.DataDtoSuccess(UserDtoResult.class);
        BeanUtils.copyProperties(user, userDtoResult);
        return userDtoResult;
    }
}
