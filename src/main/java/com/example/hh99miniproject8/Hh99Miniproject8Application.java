package com.example.hh99miniproject8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class Hh99Miniproject8Application {

    public static void main(String[] args) {
        SpringApplication.run(Hh99Miniproject8Application.class, args);
    }

}
