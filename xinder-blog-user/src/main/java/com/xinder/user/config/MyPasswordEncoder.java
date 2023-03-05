package com.xinder.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * Created by xinder on 2023-01-05
 */
@Component
public class MyPasswordEncoder extends BCryptPasswordEncoder {

    private final static Logger logger = LoggerFactory.getLogger(MyPasswordEncoder.class);


    // 密码加密
    @Override
    public String encode(CharSequence rawPassword) {
//        return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
        return super.encode(rawPassword);
    }

    // 检验密码
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        String pwd = rawPassword.toString();
        logger.info("[MyPasswordEncoder.matches]前端传来  明文密码:" + pwd);
//        logger.info("[MyPasswordEncoder.matches]前端传来  加密密码:" + DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes()));
        logger.info("[MyPasswordEncoder.matches]前端传来  加密密码:" + BCrypt.hashpw(rawPassword.toString(), encodedPassword));
        logger.info("[MyPasswordEncoder.matches]后端校验  加密密码:" + encodedPassword);

        // 传来密码如果与加密密码一样，则是qq登录
        if (pwd.equals(encodedPassword)) {
            return true;
        }
        return super.matches(rawPassword, encodedPassword);
    }


}
