package com.xinder.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.server.ServerEndpoint;

/**
 * 首先需要注入 ServerEndpointExporter ， 这个bean会自动注册使用 @ServerEndpoint
 * 的注解来声明WebSocket endpoint。
 * 注意：如果使用独立的Servlet容器，而不是直接使用SpringBoot内置容器，就不需要注入
 * ServerEndpointExporter，因为他将有容器自己提供和管理。
 *
 * @author Xinder
 * @date 2023-02-14 16:59
 */
@Configuration
public class WebSocketConfig {


    /**
     * 扫描注册使用 @ServerEndpoint 注解的类
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
