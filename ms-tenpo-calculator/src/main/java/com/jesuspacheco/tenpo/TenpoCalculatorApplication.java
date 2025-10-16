package com.jesuspacheco.tenpo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class TenpoCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenpoCalculatorApplication.class, args);
    }
}
