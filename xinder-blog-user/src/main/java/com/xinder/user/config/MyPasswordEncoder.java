package com.xinder.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @作者 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@Component
public class MyPasswordEncoder extends BCryptPasswordEncoder {

    private final static Logger logger = LoggerFactory.getLogger(MyPasswordEncoder.class);

    @Override
    public String encode(CharSequence rawPassword) {
        return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        String pwd = rawPassword.toString();
        logger.info("[MyPasswordEncoder.matches]前端传来  明文密码:" + pwd);
//        logger.info("[MyPasswordEncoder.matches]前端传来  加密密码:" + DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes()));
        logger.info("[MyPasswordEncoder.matches]前端传来  加密密码:" + BCrypt.hashpw(rawPassword.toString(), encodedPassword));
        logger.info("[MyPasswordEncoder.matches]后端校验  加密密码:" + encodedPassword);


        return super.matches(rawPassword, encodedPassword);
    }


}
