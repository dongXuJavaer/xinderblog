package com.xinder.user.config.handler.logout;

import com.xinder.api.enums.ResultCode;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.common.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Xinder
 * @date 2023-01-09 10:08
 */
@Component
public class CustomerLogoutSuccessHandler implements LogoutSuccessHandler {

    private final static Logger logger = LoggerFactory.getLogger(CustomerLogoutSuccessHandler.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        org.springframework.security.core.userdetails.User user = null;
        if (authentication != null) {
            user = (User) authentication.getPrincipal();

            Boolean delete = redisTemplate.delete(user.getUsername());

            String log ;
            if (delete != null && delete){
                logger.info("[CustomerLogoutSuccessHandler.onLogoutSuccess] 用户 [{}] 退出登录成功。", user.getUsername());
            }else {
                logger.error("[CustomerLogoutSuccessHandler.onLogoutSuccess] 用户 [{}] 退出登录失败。", user.getUsername());
            }


            BaseResponse baseResponse = BaseResponse.success(
                    ResultCode.LOGOUT_SUCCESS.getCode(),
                    ResultCode.LOGOUT_SUCCESS.getDesc());
            ResponseUtils.respJson(response, baseResponse);
        }
    }

}
