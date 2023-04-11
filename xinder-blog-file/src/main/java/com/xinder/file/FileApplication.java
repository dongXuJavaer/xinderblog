package com.xinder.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Xinder
 * @date 2023-04-11 16:11
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = "com.xinder.file.mapper")
@EnableFeignClients
public class FileApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }
}
