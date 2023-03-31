package com.xinder.article.config;

import com.xinder.common.constant.CommonConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 解决远程调用时cookie不传递的问题
 * @author Xinder
 * @date 2023-03-21 22:42
 */
@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 获取原请求
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                // 原请求 也就是http://order.gulimall.com/toTrade
                HttpServletRequest request = attributes.getRequest();
                // 获取原请求中携带的Cookie请求头
                String cookie = request.getHeader("Cookie");
                String token = request.getHeader(CommonConstant.HTTP_HEADER_TOKEN);
                // 将cookie 同步到新的请求的请求头中
                template.header("Cookie", cookie);
                template.header(CommonConstant.HTTP_HEADER_TOKEN, token);
            }
        };
    }
}
