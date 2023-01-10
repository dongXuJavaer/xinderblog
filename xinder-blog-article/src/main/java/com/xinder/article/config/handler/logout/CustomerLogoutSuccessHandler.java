package com.xinder.article.config.handler.logout;

import com.xinder.api.enums.ResultCode;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.article.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        org.springframework.security.core.userdetails.User user = null;
        if (authentication != null) {
            user = (User) authentication.getPrincipal();
            logger.info("[CustomerLogoutSuccessHandler.onLogoutSuccess] 用户 [{}] 退出登录。", user.getUsername());

            BaseResponse baseResponse = BaseResponse.success(
                    ResultCode.LOGOUT_SUCCESS.getCode(),
                    ResultCode.LOGOUT_SUCCESS.getDesc());
            ResponseUtils.respJson(response, baseResponse);
        }
    }

}
