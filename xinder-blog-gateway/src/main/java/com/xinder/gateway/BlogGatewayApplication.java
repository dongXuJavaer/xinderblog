package com.xinder.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @author Xinder
 * @date 2023-01-12 21:24
 */
@SpringBootApplication(exclude = RedisAutoConfiguration.class)
//@EnableDiscoveryClient
public class BlogGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogGatewayApplication.class);
    }
}
