package com.fruit.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用程序启动类
 * 程序的入口点
 */
@SpringBootApplication // 核心注解：开启 Spring Boot 的自动配置、组件扫描等功能
public class FruitManagementSystemApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(FruitManagementSystemApplication.class, args);
    }

}
