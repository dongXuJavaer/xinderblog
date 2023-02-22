package com.xinder.article;

import com.xinder.common.util.TokenDecode;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling//开启定时任务支持
@MapperScan("com.xinder.article.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.xinder.article.feign")
@EnableElasticsearchRepositories(basePackages = "com.xinder.article.mapper")
public class BlogArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogArticleApplication.class, args);
    }

//    @Bean
//    public RedisTemplate redisTemplate(){
//        return new RedisTemplate();
//    }

    @Bean
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }


}
