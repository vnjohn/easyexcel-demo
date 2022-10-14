package com.vnjohn.easyexcel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.vnjohn.*.mapper")
@SpringBootApplication
public class EasyexcelDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyexcelDemoApplication.class, args);
    }

}
