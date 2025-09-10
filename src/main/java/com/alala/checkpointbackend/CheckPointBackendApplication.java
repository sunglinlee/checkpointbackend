package com.alala.checkpointbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CheckPointBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckPointBackendApplication.class, args);
    }

}
