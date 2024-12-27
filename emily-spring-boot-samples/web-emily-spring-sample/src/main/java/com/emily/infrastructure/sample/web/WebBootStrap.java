package com.emily.infrastructure.sample.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Emily
 */
@EnableAsync
@SpringBootApplication
public class  WebBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(WebBootStrap.class, args);
    }

}
