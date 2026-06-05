package com.xc.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan({
        "com.xc.basic", // 本平台扫描
        "com.xc.api", // 接口api
        "com.xc.core.config.aspect", // 切面拦截，日志输出
        "com.xc.core.config.error", // 异常处理
        "com.xc.core.config.myBatis", // myBatis配置
        "com.xc.core.config.redis", // redis 配置
        "com.xc.core.config.swagger", // swagger 配置
        "com.xc.core.config.http", // http跳转https配置
})
public class BasicServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicServerApplication.class, args);
    }

}
