package com.ecjtu.hht.rolling_log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//开启异步
@EnableAsync
public class RollingLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(RollingLogApplication.class, args);
    }

}
