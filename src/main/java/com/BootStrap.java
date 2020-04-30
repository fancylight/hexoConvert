package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <h3>hexoConvert</h3>
 *
 * @author : ck
 * @date : 2020-04-30 18:10
 **/
@SpringBootApplication
public class BootStrap {
    public static void main(String[] args) {
        new SpringApplication(BootStrap.class).run(args);
    }
}
