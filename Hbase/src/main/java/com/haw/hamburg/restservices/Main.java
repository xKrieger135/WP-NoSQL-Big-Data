package com.haw.hamburg.restservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by nosql on 5/4/16.
 */
@SpringBootApplication
@ComponentScan("com.haw.hamburg")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }
}
