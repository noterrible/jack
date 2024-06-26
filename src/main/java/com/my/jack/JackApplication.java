package com.my.jack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching//支持注解实现缓存
public class JackApplication {
    public static void main(String[] args) {
        SpringApplication.run(JackApplication.class,args);
        log.info("项目成功启动，请访问'http://localhost:8080/backend/page/login/login.html'");
        log.info("移动端请访问'http://localhost:8080/front/page/login.html'");
        log.info("swagger文档地址'http://localhost:8080/doc.html'");
    }
}
