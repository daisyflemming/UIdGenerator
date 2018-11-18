package com.daisyflemming.restful.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    /**
     * This is a SpringBootApplication is a rest service used to provision UID between 4-6 charcaters long.
     * @param args no arguments required.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
