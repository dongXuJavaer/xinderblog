package com.xinder.user.config.handler.logout;

import com.xinder.api.enums.ResultCode;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.common.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Xinder
 * @date 2023-01-08 23:20
 */
@Component
public class CustomerLogoutHandler implements LogoutHandler {

    private final static Logger logger = LoggerFactory.getLogger(CustomerLogoutHandler.class);

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        org.springframework.security.core.userdetails.User user = null;
        if (authentication != null) {
            user = (User) authentication.getPrincipal();
        } else {
            BaseResponse baseResponse = BaseResponse.fail(
                    ResultCode.LOGOUT_FAIL.getCode(),
                    ResultCode.LOGOUT_FAIL.getDesc());
            ResponseUtils.respJson(response, baseResponse);
        }

        logger.info("[CustomerLogoutHandler.logout] 用户 [{}] 退出登录。",
                user == null ? null : user.getUsername());
    }
}
