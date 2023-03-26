package com.xinder.gateway.filtet;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 认证拦截器，实现了全局拦截器（GlobalFilter）接口与可排序接口（Ordered）
 *
 * @author dong
 * @since 2022-09-05 14:10
 */
@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private final static Logger logger = LoggerFactory.getLogger(AuthorizeFilter.class);

    // 令牌头名字
    private static final String AUTHORIZE_TOKEN = "Authorization";

    // 用户登录地址，未携带令牌则跳转到登录页面
//    private static final String USER_LOGIN_URL = "http://localhost:80/oauth/login";
    private static final String USER_LOGIN_URL = "http://localhost:9000/login";


    /*
        手动放行的路径
     */
    private static final String USERID_PATH = "/api/user/"; // 根据uid查询用户的路径
    private static final String USER_ADD_GROUP_PATH = "/api/group/user/add/list/"; // 根据uid查询用户加入的群聊路径

    // 访问用户Api下，无需令牌的路径
    private static final ArrayList<String> THROUGH_USER_PATH = new ArrayList<String>() {{
        add("/api/user/num");
        add("/api/user/login");
    }};
    // 访问评论Api下，无需令牌的路径
    private static final ArrayList<String> THROUGH_COMMENTS_PATH = new ArrayList<String>() {{
        add("/api/comments/num");
        add("/api/comments/list");
    }};

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

        log.info("请求路径--{}", path);
//        System.out.println("请求路径---------" + path);


        AtomicBoolean throughFlag = new AtomicBoolean(false);  // 直接放行的标志

        // 如果请求是blog等开放服务，则直接放行
        if (path.startsWith("/api/article/")
                || judgeThroughFlag(THROUGH_USER_PATH, path)
                || judgeThroughFlag(THROUGH_COMMENTS_PATH, path)
                || path.startsWith("/api/tags")
                || path.startsWith("/api/group/list")
                || path.startsWith("/api/tags/all")
                || path.startsWith("/api/category/all")
                || judgeRestGet(USERID_PATH, path)
                || judgeRestGet(USER_ADD_GROUP_PATH, path)
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

    // 判断是否是 可以直接放行的路径
    private boolean judgeThroughFlag(List<String> list, String path) {
        for (String s : list) {
            if (path.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否是【根据id获取】的rest请求风格的请求路径
    private boolean judgeRestGet(String rootPath, String path) {

        try {
            return Optional.of(path)
                    .filter(str -> str.startsWith(rootPath))
                    .map(str -> path.substring(rootPath.length()))
                    .map(Long::parseLong).isPresent();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
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
