package com.xinder.article.auth.login;

import com.alibaba.fastjson.JSONObject;
import com.xinder.api.response.base.BaseResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Xinder
 * @date 2023-01-08 13:18
 */
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //打印登录校验失败信息
        exception.printStackTrace();
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out=response.getWriter();
        BaseResponse result = new BaseResponse();
        result.setSuccess(false);
        result.setMsg("登录失败");

        out.write(JSONObject.toJSONString(result));
        out.flush();
        out.close();
    }
}
