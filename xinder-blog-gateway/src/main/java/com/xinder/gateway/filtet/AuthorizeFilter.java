package com.xinder.gateway.filtet;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 认证拦截器，实现了全局拦截器（GlobalFilter）接口与可排序接口（Ordered）
 *
 * @author dong
 * @since 2022-09-05 14:10
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    // 令牌头名字
    private static final String AUTHORIZE_TOKEN = "Authorization";

    // 用户登录地址，未携带令牌则跳转到登录页面
    private static final String USER_LOGIN_URL = "http://localhost:80/oauth/login";


    /**
     * 全局过滤器，用来验证用户是否有token（这里只判断有无）
     * 然后加上'Bearer '返回给调用者  之后调用者再拿着token访问对应的微服务（也就是资源服务器），由对应的微服务用公钥对token进行验证
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 登录不被拦截
        //获取请求的URI  api/user/login?username=zhangsan9527&password=123456
        String path = request.getURI().getPath();

        // 如果请求是blog等开放服务，则直接放行
        if (path.startsWith("/api/article/")
                || path.startsWith("/api/user/login")
                || path.startsWith("/api/tags")
        ) {
            // 放行
            Mono<Void> filter = chain.filter(exchange);
            return filter;
        }

        // 1.获取头文件中的令牌信息  优先存储位置
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        // 2.如果没有，则获取参数中的令牌信息
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }

        // 3. 如没有，则从Cookie中获取令牌信息
        HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
        if (cookie != null) {
            token = cookie.getValue();
        }
        // 如果都没有获取到令牌，则抛出错误 405
        if (StringUtils.isEmpty(token)) {
//            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            System.out.println("没有令牌");
//            return response.setComplete();
            String url = null;
            try {
                url = URLEncoder.encode(request.getURI().toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return needAuthorization(USER_LOGIN_URL + "?from=" + url, exchange);

        }

        try {
            // 解析令牌数据
//            Claims claims = JwtUtil.parseJWT(token);
            // 不用解析令牌了，因为现在是通过私钥加密后的令牌，直接转发
            // 转发给对应的微服务，其自己用公钥解析
            if (token != null) {
                if (!token.substring(0, 6).toLowerCase().startsWith("bearer")) {
                    //添加Bearer开头
//                    token = "Bearer " + token;
                }
            }
            //把token转发到对应微服务
            request.mutate().header(AUTHORIZE_TOKEN, token);
        } catch (Exception e) {
            e.printStackTrace();
            // 解析失败，响应401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

    private Mono<Void> needAuthorization(String url, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        // 303 表示去其他页面的状态码
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location", url);
        return response.setComplete();
    }


    /**
     * 过滤器执行顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

}
