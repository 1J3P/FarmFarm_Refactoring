package com.example.farmfarm_refact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableJpaAuditing
@EnableScheduling
//@EnableJpaRepositories(basePackages = "com.example.farmfarm_refact.repository")
public class FarmfarmRefactApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmfarmRefactApplication.class, args);
    }

}
