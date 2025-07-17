package com.career.testtaskmegacom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TestTaskMegacomApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTaskMegacomApplication.class, args);
    }

}
