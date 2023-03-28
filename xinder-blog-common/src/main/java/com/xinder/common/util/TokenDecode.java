package com.xinder.common.util;

import com.alibaba.fastjson.JSON;
import com.xinder.common.constant.CommonConstant;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dong
 * @since 2022-09-08 14:44
 */
public class TokenDecode {
    //公钥
    private static final String PUBLIC_KEY = "public.key";
    //定义读取公钥内容变量
    private static String publickey = "";

    //读取公钥内容方法
    private String getPubKey() {
        Resource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            //读取第一行公钥数据
            if ((publickey = bufferedReader.readLine()) != null) {
                return publickey;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return publickey;
    }

    /**
     * 读取令牌数据
     *
     * @param token 令牌
     * @return 返回解析后的数据
     */
    public Map<String, Object> dcodeToken(String token) {
        //校验并解析Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(this.getPubKey()));
        //获取Jwt原始内容
        String claims = jwt.getClaims();
        return JSON.parseObject(claims, Map.class);
    }

    /***
     * 从容器中获取用户信息
     * @return
     */
    public Map<String, Object> getUserInfo() {

        String token = getToken();
        // 如果token为空，新建一个map返回(防止NPE)
        if (StringUtils.isEmpty(token)){
            return new HashMap<>();
        }
        //令牌解码
        Map<String, Object> map = dcodeToken(token);
        return map;
    }


    /**
     * 获取Token
     *
     * @return
     */
    public String getToken() {
        //获取授权信息  springSecurity上下文对象获取认证详情，OAuth2AuthenticationDetails详情需要符合Oauth2格式
        //        SecurityContext context = SecurityContextHolder.getContext();
        //        Authentication authentication = context.getAuthentication();
        //        Object details1 = authentication.getDetails();
        //        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)
        //                SecurityContextHolder.getContext().getAuthentication().getDetails();
        //        String tokenValue = details.getTokenValue();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        String name = CommonConstant.HTTP_HEADER_TOKEN;

        String token = "";
        if (null == cookies) {
            token = "";
        }
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (name.equals(cookies[i].getName())) {
                    token = cookies[i].getValue();
                }
            }
        }
        // 从请求头中获取令牌
        if (token.equals("")) {
            token = request.getHeader(name);
        }
        return token;
    }


}
