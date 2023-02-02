package com.xinder.user.auth;

import com.alibaba.fastjson.JSONArray;
import com.xinder.api.enums.PermissionsEnums;
import com.xinder.api.enums.ResultCode;
import com.xinder.common.constant.CommonConstant;
import com.xinder.common.util.TokenDecode;
import com.xinder.user.exception.CheckAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 权限校验
 *
 * @author Xinder
 * @date 2023-02-02 16:25
 */
@Component
public class CheckAuth {

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 【用户管理】权限检测
     *
     * @param
     * @return
     */
    public boolean checkUserAuth() {
        Map<String, Object> userInfo = tokenDecode.getUserInfo();
        List authorities = (JSONArray) userInfo.get(CommonConstant.TOKEN_AUTHORITIES);
        boolean contains =
                authorities.contains("ROLE_" + PermissionsEnums.ADMINISTRATORS.getValue());
        if (!contains) {
            throw new CheckAuthException(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getDesc());
        }
        return true;
    }

}
