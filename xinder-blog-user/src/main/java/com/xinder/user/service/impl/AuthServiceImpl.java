package com.xinder.user.service.impl;

import com.xinder.common.util.AuthToken;
import com.xinder.user.config.MyPasswordEncoder;
import com.xinder.user.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

/**
 * 用户认证服务
 * @author Xinder
 * @date 2023-01-08 11:13
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Value("${feign.client.user.name}")
    private String userServerName;


    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        // 申请令牌
        AuthToken authToken = this.applyToken(username, password, clientId, clientSecret);
        if(authToken == null){
            throw new RuntimeException("申请令牌失败");
        }

        return authToken;
    }


    /****
     * 认证方法，申请token
     * @param username:用户登录名字
     * @param password：用户密码
     * @param clientId：配置文件中的客户端ID
     * @param clientSecret：配置文件中的秘钥
     * @return
     */
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {

        // 远程调用服务, 选中认证服务的地址
        ServiceInstance instance = loadBalancerClient.choose(userServerName.toLowerCase());
        if (instance == null) {
            throw new RuntimeException("找不到认证服务");
        }
        //获取令牌的url
        String url = instance.getUri().toString() + "/oauth/token";
//        String url = "https://www.baidu.com";

        //封装body里面的参数
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        //授权方式
        formData.add("grant_type", "password");
        //账号
        formData.add("username", username);
        //密码
        formData.add("password", password);

        //封装头
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Authorization", this.httpbasic(clientId, clientSecret));

        //内部类重写快捷键 alt+insert
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // 当发生错误的时候，只有状态码不是400或者401才返回错误,其他情况正常响应
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });
        Map body = null;
        try {
            // http请求spring security的申请令牌接口? （感觉这里是请求的Oauth，申请令牌）
            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST,
                    new HttpEntity<MultiValueMap<String, String>>(formData, header), Map.class);
            // 获取响应结果
            body = responseEntity.getBody();
            logger.info("请求token的响应结果：{}",body);
            System.out.println(body.get("access_token"));
            try {
                if (body == null || body.get("access_token") == null ||
                        body.get("jti") == null || body.get("refresh_token") == null) {
                    System.out.println("请求body响应信息 :" + body);
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException("令牌创建失败");
            }

        }catch (RestClientException e) {
            throw new RuntimeException(e);
        }

        // 将响应数据封装成AuthToken
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(body.get("access_token").toString());
        authToken.setRefreshToken(body.get("refresh_token").toString());
        authToken.setJti(body.get("jti").toString());
        return authToken;
    }

    /***
     * base64编码
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String httpbasic(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码” 就是httpbasic
        String string = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic " + new String(encode); // 返回的认证的请求头格式，比如：Basic Y2xpZW50OjEyMzQ1Ng==
    }

}
