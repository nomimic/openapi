package com.tistory.nomimic.openapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Lucas,Lee on 14. 11. 14..
 */
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
